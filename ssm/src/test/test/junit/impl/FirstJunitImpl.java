package test.junit.impl;

import com.zsf.domain.User;
import freemarker.template.Configuration;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import test.junit.BaseJunitTest;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FirstJunitImpl extends BaseJunitTest {

    static int i = 0;
    public FirstJunitImpl() {
        super();
    }

    @Before
    public void test() {
        System.out.println("before test");
    }

    @Test(timeout = 4000, expected = IOException.class)
    public void AT1() throws Exception{
        //Thread.sleep(3000);
        System.out.println("test " + ++i);
        throw new Exception();
    }

    @Test
    public void AT2() {
        System.out.println("test2 " + ++i);
    }

    @After
    public void name() {
        System.out.println("After test");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @Test
    public void test2() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.qq.com");
        sender.setUsername("384546294@qq.com");
        sender.setPassword("uvyvamvkscsybjcc");

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setSubject("test:html");
            helper.setFrom("384546294@qq.com");
            helper.setText(getHtml(), true);
            helper.setTo("roosteracham@outlook.com");
            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String getHtml() {
        String filePath = "D:\\a.html";
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br=new BufferedReader(new InputStreamReader(new FileInputStream(filePath),
                    "UTF-8"));
            String temp = null;
            while((temp = br.readLine())!=null){
                sb.append(temp);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (sb.toString());
    }

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    Configuration freemarkerConfiguration;

    @Value("${mailSender.username}")
    private String from;

    public String geFreeMarkerTemplateContent(Map<String, Object> model)
    {
        StringBuffer content = new StringBuffer();
        try
        {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate("t1.txt"), model));
            return content.toString();
        }
        catch (Exception e)
        {
            System.out.println(("Exception occured while processing fmtemplate:" + e.getMessage()));
        }
        return "";
    }

    @Test
    public void sendMail() throws Exception{
        //((JavaMailSenderImpl)mailSender).setPassword("uvyvamvkscsybjcc");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo("roosteracham@outlook.com");
        helper.setSubject("test:freemaker");
        Map<String, Object> model = new HashMap<String, Object>();
        User user = new User();
        user.setId(1);
        user.setPass("pass");
        user.setName("name");
        model.put("user", user);
        String text = "text"/*geFreeMarkerTemplateContent(model)*/;//Use Freemarker
        helper.setText(text, true);/*
        helper.addAttachment("userdel.png",
                new ClassPathResource("imgs/userdel.png"));*/
        mailSender.send(message);
    }

}
