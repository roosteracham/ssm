package test.junit.impl;

import com.alibaba.fastjson.JSON;
import com.zsf.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.junit.BaseJunitTest;

public class AopTest extends BaseJunitTest {

    @Autowired
    private com.zsf.business.AopTest aopTest;

    @Test
    public void test1() {

        //aopTest.test1();
        //aopTest.setTest1();
        aopTest.afterReturn("param");
    }

    public static void main(String[] args) {
        User user = new User();
        user.setName("asdasd");
        user.setPass("123a");
        user.setId(1);
        System.out.println(JSON.toJSON(user));
    }
}
