package test.junit.impl;

import com.zsf.domain.User;
import com.zsf.service.IUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import test.junit.BaseJunitTest;

import javax.servlet.http.HttpSession;

public class SecondJunitImpl extends BaseJunitTest{ // 继承BaseJunitTest

    @Autowired
    private IUserService iUserService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        System.out.println();
        User user = iUserService.selectByPrimaryKey(1);
        System.out.println(user.getName() + " " + user.getPass());
    }

    @Test
    public void insert() {
        User user = new User();
        user.setName("qd");
        user.setPass("qsp");
        iUserService.insert(user);
        int a = 1/0;
    }

    @Test
    public void redisTest() {
        System.out.println(redisTemplate.opsForValue().get("sb"));;
    }

    @Test
    public void pathTest() {
        System.out.println();
    }
}
