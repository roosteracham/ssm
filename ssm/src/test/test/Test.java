package test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
public class Test {
    public static void main(String[] args) throws Exception{
        InputStream inputStream =
                Resources.getResourceAsStream("org/fkit/config/mybatis/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        /*User person = new User();
        person.setUsername("5");
        person.setPassword("5");
        //session.insert("org.fkit.mapper.IUserMapper.save", person);
        person = session.selectOne("org.fkit.mapper.IUserMapper.selectById", 4);
        */session.commit();
        session.close();
    }
}
