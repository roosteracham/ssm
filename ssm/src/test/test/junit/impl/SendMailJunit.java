package test.junit.impl;

import com.zsf.domain.User;
import com.zsf.domain.UserInfo;
import com.zsf.service.IMailService;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
import test.junit.BaseJunitTest;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

public class SendMailJunit extends BaseJunitTest {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${mailSender.username}")
    private String from;

    private final String to = "roosteracham@outlook.com";

    @Autowired
    private IMailService mailBusiness;

    @Test
    public void test1() {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

                UserInfo user = mailBusiness.selectById(16);
                message.setSubject("贺卡");
                message.setTo("roostera@163.com");
                /*String[] strings = new String[2];
                strings[0] = "roostera@163.com";
                strings[1] = from;
                message.setTo(strings);*/
                message.setFrom(from); // could be parameterized...
                Map model = new HashMap();
                model.put("user", user);
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine,
                        "velocities/mail.vm",
                        "UTF-8",
                        model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
        System.out.println("sended");
    }
}
