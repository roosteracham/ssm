package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zsf.domain.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception{
        /*InputStream inputStream =
                Resources.getResourceAsStream("org/fkit/config/mybatis/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        *//*User person = new User();
        person.setUsername("5");
        person.setPassword("5");
        //session.insert("org.fkit.mapper.IUserMapper.save", person);
        person = session.selectOne("org.fkit.mapper.IUserMapper.selectById", 4);
        *//*session.commit();
        session.close();*/

        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("config/spring/spring-*.xml");
        System.out.println(applicationContext.getBean("user", User.class).getName());
    }

    @org.junit.Test
    public void test() throws Exception{

        HttpHost host = new HttpHost("localhost", 80);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("/zutai/dev/testn");
        CloseableHttpResponse response = client.execute(host, request);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = response.getEntity();
            String message = EntityUtils.toString(resEntity, "utf-8");
            System.out.println(message);
        } else {
            System.out.println("请求失败");
        }
    }

    @org.junit.Test
    public void test2() {
        List<NameValuePair> pairs = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setPass("pwd123");
        user.setName("asdas");

        JSONObject object = new JSONObject();
        object.put("name", "name123");
        object.put("pass", "pwd123");
        object.put("id", 1);
        //pairs.add(new BasicNameValuePair("data", JSON.toJSON(user).toString()));
        try {
            //UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            //entity.setContentType("application/json");
            StringEntity entity = new StringEntity(object.toString(), "utf-8");
            RequestConfig config = RequestConfig.custom().setConnectTimeout(10 * 1000)
                    .setSocketTimeout(10 * 1000).setConnectionRequestTimeout(5 * 1000)
                    .build();

            HttpClient client = HttpClients.createDefault();
            HttpPost get = new HttpPost("http://localhost/zutai/dev/testn");
            //get.setHeader("Content-Type", "application/json;charset=utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            get.setEntity(entity);
            get.setConfig(config);
            HttpResponse response = client.execute(get);
                if (200 == response.getStatusLine().getStatusCode()) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            } else {
                System.out.println("failed");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
