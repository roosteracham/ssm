package com.zsf.business;

import com.zsf.dao.RedisDao;
import com.zsf.dao.UserInfoDao;
import com.zsf.domain.ConfirmRegisterDto;
import com.zsf.domain.UserInfo;
import com.zsf.service.*;
import com.zsf.util.encode.BASE64;
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
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;
import sun.misc.BASE64Encoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailBusiness {

    private Logger logger = LoggerFactory.getLogger(MailBusiness.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${mailSender.username}")
    private String from;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private UserInfoDao userInfoDao;

    // 发送确认邮件
    public void sendEmail(UserInfo userInfo) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage)
                    throws MessagingException {
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
                    return;
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

    private void getUrl(ConfirmRegisterDto confirmRegisterDto) {

        try {
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String param = "name:" + confirmRegisterDto.getName() + ";url:" +
                    BASE64.base64Encode(base64Encoder);

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

}