package test;

import com.zsf.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
}
