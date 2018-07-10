package test.junit.impl;

import com.zsf.domain.User;
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

    @Test
    public void test1() {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
                User user = new User();
                user.setName("users name");
                user.setId(2018);
                user.setPass("http://localhost:3000/user/test");
                message.setTo(to);
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
