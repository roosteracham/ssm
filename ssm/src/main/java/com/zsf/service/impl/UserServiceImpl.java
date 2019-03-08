package com.zsf.service.impl;

import com.alibaba.fastjson.JSON;
import com.zsf.business.MailBusiness;
import com.zsf.dao.UserInfoDao;
import com.zsf.dao.UserSvgsDao;
import com.zsf.domain.*;
import com.zsf.service.IUserService;
import com.zsf.service.RedisService;
import com.zsf.util.encode.BASE64;
import com.zsf.util.errorcode.ErrorCodeEnum;
import com.zsf.util.errorcode.MailServerEnum;
import com.zsf.util.errorcode.RedirectEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.net.util.Base64;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;
import sun.misc.BASE64Encoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements IUserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    public MailBusiness mailBusiness;

    @Autowired
    public UserSvgsDao userSvgsDao;

    /**
     *  登陆
     * @param user
     * @param response
     * @return
     */
    @Override
    public ResBody login(UserInfo user, HttpServletResponse response) {

        ResBody body = new ResBody();
        body.setSuccess(false);
        UserInfo userInfo = userInfoDao.selectByName(user);
        if (null == userInfo) {
            logger.warn("【" + Thread.currentThread().getName() +
                        "】 不存在用户");
            body.setData("未注册用户");
        } else if (3 != userInfo.getChecked()) {
            logger.warn("【" + Thread.currentThread().getName() +
                    "】 邮箱未确认");
            body.setData("邮箱未确认");
        } else if (!StringUtils.isEmpty(user.getAuth()) &&
                !userInfo.getAuth().equals(user.getAuth())){
            logger.warn("【" + Thread.currentThread().getName() +
                    "】 密码错误");
            body.setData("密码错误");
        } else {

            // token设置
            String token = setToken(response, userInfo);
            TokenLocation tokenLocation = new TokenLocation();
            tokenLocation.setLocation(RedirectEnum.RUN);
            tokenLocation.setToken(token);
            logger.info("【" + Thread.currentThread().getName() +
                    "】 用户：" + userInfo.getName() + " 登陆成功！");
            body.setSuccess(true);
            body.setData(JSON.toJSONString(tokenLocation));
        }
        return body;
    }

    @Override
    public ResBody roleManage(UserSvgsDto userSvgsDto) {
        UserInfo userInfo = userInfoDao.selectById(userSvgsDto.getAuthId());
        if (userInfo.getRole() == 3) {

            List<Integer> svgs = userSvgsDao.selectSvgsByUserId(userInfo.getId());
            UserSvgs userSvgs = new UserSvgs();
            userSvgs.setUserId(userInfo.getId());
            for(Integer id : userSvgsDto.getSvgIds()) {
                if (!svgs.contains(id)) {
                    userSvgs.setSvgId(id);
                    userSvgsDao.insert(userSvgs);
                }
            }

        }
        ResBody body = new ResBody();
        body.setData("");
        body.setSuccess(true);
        return body;
    }

    private String setToken(HttpServletResponse response, UserInfo userInfo) {
        try {
            String key = userInfo.getId() + "";
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String value = BASE64.base64Encode(base64Encoder);
            // token放在redis里面，60分钟过期
            redisService.setExpired(key, value, 60, TimeUnit.MINUTES);
            // 设置token
            Cookie cookie = new Cookie("token", key);
            // cookie 一天过期
            cookie.setMaxAge(60 * 60 * 24);
            cookie.setPath("/");
            response.addCookie(cookie);
            return key;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("【 " + Thread.currentThread().getName() +
                    "】 exception : ", e.getMessage());
        }
        return null;
    }

    /**
     * @param userInfo
     * @return 0 未注册 1 用户名已存在 2 邮箱已存在
     */
    private int isRegistered(UserInfo userInfo) {
        UserInfo existName = userInfoDao.selectByName(userInfo);
        if (existName != null) {
            return 1;
        }
        UserInfo existEmail = userInfoDao.selectByEmail(userInfo);
        if (existEmail!= null) {
            return 2;
        }
        return 0;
    }

    /**
     * 确认注册
     *
     * @param param
     * @return
     */
    @Override
    public String confirm(String param, HttpServletRequest request) {
        // 获取ID
        String stringDecoded = new String(Base64.decodeBase64(param));
        String[] params = stringDecoded.split(";");
        String name = params[0].split(":")[1];

        // 设置id用户确认
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo = userInfoDao.selectByName(userInfo);

        Date now = Calendar.getInstance().getTime();
        StringBuilder directUrl = new StringBuilder();
        if (now.before(userInfo.getExpireTime())) {
            if (2 == userInfo.getChecked()) {
                userInfo.setChecked(3);
                userInfoDao.updataById(userInfo);
                directUrl.append(RedirectEnum.LOGIN);
            } else {
                logger.warn("【 " + Thread.currentThread().getName() +
                        "】 注册确认出错");
                directUrl.append(RedirectEnum.ERROR);
            }
        } else {
            // 提示过期，重新发送邮件
            logger.info("【 " + Thread.currentThread().getName() +
                    "】 注册确认连接过期，重新发送");
            if (2 == userInfo.getChecked()) {
                logger.info("【 " + Thread.currentThread().getName() +
                        "】 重新发送确认邮件");
                userInfo.setExpireTime(DateUtils.addDays(now, 1));
                userInfoDao.updataById(userInfo);
                mailBusiness.sendEmail(userInfo);
                directUrl.append(RedirectEnum.LOGIN);
            } else {
                logger.warn("【 " + Thread.currentThread().getName() +
                        "】 注册确认出错, 状态是：【" + userInfo.getChecked() + "】");
                directUrl.append(RedirectEnum.ERROR);
            }
        }

        return directUrl.toString();
    }

    /**
     * 注册
     *
     * @param userInfo
     * @return
     */
    @Override
    public ResBody register(UserInfo userInfo) {

        // 设置返回参数
        ResBody body = new ResBody();
        body.setSuccess(false);
        body.setErrorCode(ErrorCodeEnum.SUC.getIndex());
        body.setData("");

        // 确认是否已存在用户
        int existCode = isRegistered(userInfo);
        switch (existCode) {
            case 1: // 用户名存在
                body.setData("用户名已存在");
                body.setErrorCode(ErrorCodeEnum.USERNAME_EXIST.getIndex());
                break;
            case 2: // 邮箱已注册
                body.setData("邮箱已注册");
                body.setErrorCode(ErrorCodeEnum.USER_EMAIL_EXIST.getIndex());
                break;
            default: // 未存在用户

                // 截取邮箱服务器类型
                String emailType = userInfo.getEmailAddr().split("@")[1];
                int index = MailServerEnum.getEmailServer(emailType);
                if (-1 == index) {
                    body.setData("不支持此类邮箱，请使用QQ、163、outlook邮箱注册！");
                } else {
                    Date date = Calendar.getInstance().getTime();
                    userInfo.setExpireTime(DateUtils.addDays(date, 1));
                    userInfo.setChecked(1);
                    // 先插一条记录
                    userInfoDao.insertSelective(userInfo);
                    // 发送确认邮件
                    mailBusiness.sendEmail(userInfo);
                    EmailAddrDto emailAddrDto = new EmailAddrDto();
                    emailAddrDto.setIndex(index);
                    emailAddrDto.setLocation(RedirectEnum.EMAIL_SENT);
                    body.setSuccess(true);
                    body.setData(JSON.toJSONString(emailAddrDto));
                }
        }

        return body;
    }
}
