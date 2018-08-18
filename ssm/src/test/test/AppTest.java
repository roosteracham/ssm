package test;

import static org.junit.Assert.assertTrue;

import com.zsf.business.MailBusiness;
import com.zsf.domain.User;
import com.zsf.domain.UserInfo;
import com.zsf.service.IMailService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.net.util.Base64;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import sun.misc.BASE64Encoder;
import test.junit.BaseJunitTest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
/**
 * Unit test for simple App.
 */
public class AppTest extends BaseJunitTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void sendEmail() {

        //配置发送邮件的环境属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        //smtp登陆的账号、密码 ；需开启smtp登陆
        props.put("mail.user", "384546294@qq.com");
        // 访问SMTP服务时需要提供的密码,不是邮箱登陆密码，一般都有独立smtp的登陆密码
        props.put("mail.password", "zhangbug10661216");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };

        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        try{
            InternetAddress form = new InternetAddress(
                    props.getProperty("mail.user"));
            message.setFrom(form);

            // 设置收件人
            String mailAdr = "roosteracham@yahoo.com";
            InternetAddress to = new InternetAddress(mailAdr);
            message.setRecipient(Message.RecipientType.TO, to);

            // 设置抄送
            //InternetAddress cc = new InternetAddress("luo_aaaaa@yeah.net");
            //message.setRecipient(RecipientType.CC, cc);

            // 设置密送，其他的收件人不能看到密送的邮件地址
            //InternetAddress bcc = new InternetAddress("aaaaa@163.com");
            //message.setRecipient(RecipientType.CC, bcc);

            // 设置邮件标题
            String subject = "测试邮件标题";
            message.setSubject(subject);
            // 设置邮件的内容体
            String content = "测试邮件001 <br>内容....";
            message.setContent(content, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
        }catch(MessagingException e){
            e.printStackTrace();
        }
    }

    @Test
    public void sendEmail1() {

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器

        // 获取系统属性
        final Properties properties = new Properties();

        // 收件人列表
        List<String> receivers = new ArrayList<>();
        receivers.add("roosteracham@yahoo.com");
        receivers.add("roosteracham@outlook.com");

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.setProperty("mail.smtp.auth", "true");

        // 发件人电子邮箱
        properties.setProperty("from", "384546294@qq.com");
        // 收件人电子邮箱
        //properties.setProperty("to", "roosteracham@yahoo.com");
        properties.setProperty("pwd", "zhangbug10661216");

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(properties.getProperty("from"), properties.get("pwd").toString()); //发件人邮件用户名、密码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(properties.getProperty("from")));


            // Set To: 头部头字段
            /*
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(properties.getProperty("to")));
*/
            // 多收件人
            Address[] addresses = new Address[2];
            for (int i = 0; i < 2; i++) {
                InternetAddress address = new InternetAddress(receivers.get(i));
                address.setPersonal("name" + i);
                addresses[i] = address;
            }

            message.addRecipients(Message.RecipientType.TO, addresses);
            // Set Subject: 头部头字段
            message.setSubject("This is the Subject Line!");

            for (int i = 0; i < 2; i++) {

                InternetAddress address = (InternetAddress)addresses[i];

                // 设置消息体
                message.setContent("to : " + address.getPersonal() + " <br>This is actual message multi<br>second<br><font style='color:red'>third line</font>", "text/html");

                // 发送消息
                Transport.send(message);
            }
            System.out.println("Sent message successfully....from runoob.com");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendEmailWithFile() {

        Properties properties = new Properties();
        properties.setProperty("", "");

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.setProperty("mail.smtp.auth", "true");
        // 发件人电子邮箱
        properties.setProperty("from", "384546294@qq.com");
        // 收件人电子邮箱
        //properties.setProperty("to", "roosteracham@yahoo.com");
        properties.setProperty("pwd", "zhangbug10661216");

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(properties.getProperty("from"), properties.get("pwd").toString()); //发件人邮件用户名、密码
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            // 设置头部头字段
            // set from
            message.setFrom(properties.getProperty("from"));

            // set to
            String to = "roosteracham@outlook.com";
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // set subject
            message.setSubject("title of subject");

            // 创建消息部分

            BodyPart messageBodyPart = new MimeBodyPart();

            // 消息
            messageBodyPart.setText("bodyPart message");

            // 创建多重消息
            Multipart multipart = new MimeMultipart();
            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);

            // 附件部分

            messageBodyPart = new MimeBodyPart();
            String fileName = "D:\\pom.xml";
            DataSource dataSource = new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            // 发送完整消息
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("multiple message has sent");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    IMailService mailBusiness;

    @Rollback(false)
    @Test
    public void encode() {
        String string = "abc阿瑟东";
        System.out.println(string);
        try {
            UserInfo user = new UserInfo();
            user.setName("张三");
            Calendar calendar = Calendar.getInstance();
            MessageDigest md5 = MessageDigest.getInstance("md5");

            BASE64Encoder base64Encoder = new BASE64Encoder();
            byte[] encoderMD5 = md5.digest((calendar + "").getBytes("UTF-8"));
            String encodeMD5String = new String(encoderMD5);
            user.setAuth(base64Encoder.encode(encoderMD5));
            //System.out.println(encodeMD5String);
            Date date = calendar.getTime();
            user.setExpireTime(DateUtils.addDays(date, 1));
            //System.out.println(date);
            //System.out.println(date.before(user.getExpireTime()));
            /*String param = "id:" + user.getId() + ";url:" + user.getAuth();

            String url = "http//localhost:8888/mail/greeting?param=" +
                    base64Encoder.encode(param.getBytes("UTF-8"));
            user.setUrl(url);*/
            mailBusiness.insert(user);
            /*String encoded = base64Encoder.encode(encoderMD5);
            System.out.println(base64Encoder.encode(encoderMD5).equals(encoded));
            System.out.println(encoderMD5.equals(
                    Base64.decodeBase64(encoded)));*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Rollback(false)
    @Test
    public void test2() throws Exception{
        UserInfo user = mailBusiness.selectById(16);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String param = "id:" + user.getId() + ";url:" + user.getAuth();
            /*String url = "http//localhost:8888/mail/greeting?param=" +
                    base64Encoder.encode(param.getBytes("UTF-8"));*/
        //user.setUrl(base64Encoder.encode(param.getBytes("UTF-8")));

        mailBusiness.update(user);
    }

    @Rollback(false)
    @Test
    public void test3() {

        UserInfo user = mailBusiness.selectById(16);

    }
}