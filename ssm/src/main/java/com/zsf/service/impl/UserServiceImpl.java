package com.zsf.service.impl;

import com.alibaba.fastjson.JSON;
import com.zsf.dao.UserInfoDao;
import com.zsf.domain.ConfirmRegisterDto;
import com.zsf.domain.ResBody;
import com.zsf.domain.TokenLocation;
import com.zsf.domain.UserInfo;
import com.zsf.service.IUserService;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
public class UserServiceImpl implements IUserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${mailSender.username}")
    private String from;

    @Autowired
    private RedisService redisService;

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
            tokenLocation.setLocation(RedirectEnum.CONFIG);
            tokenLocation.setToken(token);
            logger.info("【" + Thread.currentThread().getName() +
                    "】 用户：" + userInfo.getName() + " 登陆成功！");
            body.setSuccess(true);
            body.setData(JSON.toJSONString(tokenLocation));
        }
        return body;
    }

    private String setToken(HttpServletResponse response, UserInfo userInfo) {
        try {
            String key = userInfo.getId() + "";
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String value = base64Encode(base64Encoder);
            // token放在redis里面，60分钟过期
            redisService.setExpired(key, value, 60, TimeUnit.MINUTES);
            // 设置token
            Cookie cookie = new Cookie("token", key);
            // cookie 一天过期
            cookie.setMaxAge(60 * 60 * 24);
            cookie.setPath("/");
            response.addCookie(cookie);
            return key;
        } catch (NoSuchAlgorithmException e) {
            logger.error("【 " + Thread.currentThread().getName() +
                    "】 exception : ", e.getMessage());
        } catch (UnsupportedEncodingException e ) {
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

    // 发送确认邮件
    public void sendEmail(UserInfo userInfo) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage)
                    throws MessagingException{
                try {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                            "UTF-8");

                    ConfirmRegisterDto confirmRegisterDto = new ConfirmRegisterDto();
                    confirmRegisterDto.setName(userInfo.getName());
                    getUrl(confirmRegisterDto);

                    message.setSubject("确认注册");
                    message.setTo(userInfo.getEmailAddr());
                    message.setFrom(from); // could be parameterized...
                    Map model = new HashMap();
                    model.put("user", confirmRegisterDto);
                    String text = VelocityEngineUtils.mergeTemplateIntoString(
                            velocityEngine,
                            "velocities/mail.vm",
                            "UTF-8",
                            model);
                    message.setText(text, true);
                } catch (MessagingException e) {
                    logger.error("【 " + Thread.currentThread().getName() +
                            "】 ", e.getMessage());
                    throw e;
                }
            }
        };

        try {
            this.mailSender.send(preparator);
        } catch (MailException e) {
            logger.error("【 " + Thread.currentThread().getName() +
                    "】 sending confirming register email is failed");
            return;
        }

        // 更新状态已经发送邮件
        userInfo.setChecked(2);
        userInfoDao.updataByName(userInfo);
        logger.info("【 " + Thread.currentThread().getName() +
                "】 confirming register email is sent");
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
                sendEmail(userInfo);
                directUrl.append(RedirectEnum.LOGIN);
            } else {
                logger.warn("【 " + Thread.currentThread().getName() +
                        "】 注册确认出错, 状态是：【" + userInfo.getChecked() + "】");
                directUrl.append(RedirectEnum.ERROR);
            }
        }

        return directUrl.toString();
    }

    private void getUrl(ConfirmRegisterDto confirmRegisterDto) {

        try {
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String param = "name:" + confirmRegisterDto.getName() + ";url:" +
                    base64Encode(base64Encoder);

            confirmRegisterDto.setUrl(
                    base64Encoder.encode(param.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            logger.error("【 " + Thread.currentThread().getName() +
                    "】 ", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error("【 " + Thread.currentThread().getName() +
                    "】 ", e.getMessage());
        }
    }

    private String base64Encode(BASE64Encoder base64Encoder)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return base64Encoder.encode(md5Encode());
    }
    private byte[] md5Encode() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Calendar calendar = Calendar.getInstance();
        MessageDigest md5 = MessageDigest.getInstance("md5");
        return md5.digest((calendar + "").getBytes("UTF-8"));
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
                Date date = Calendar.getInstance().getTime();
                userInfo.setExpireTime(DateUtils.addDays(date, 1));
                userInfo.setChecked(1);
                // 先插一条记录
                userInfoDao.insertSelective(userInfo);
                // 发送确认邮件
                sendEmail(userInfo);
                body.setSuccess(true);
                body.setData(RedirectEnum.LOGIN);
                break;
        }

        return body;
    }
}
