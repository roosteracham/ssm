package com.zsf.business;

import com.alibaba.fastjson.JSON;
import com.zsf.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class AopTest {

    public void test1() {
        System.out.println("test method");
    }

    public void setTest1() {
        System.out.println("setTest1");
    }

    public User afterReturn(String a) {
        System.out.println(a);
        User user = new User();
        user.setName("asdasd");
        user.setPass("123a");
        user.setId(1);
        User user2 = new User();
        user2.setName("22222");
        user2.setPass("2");
        user2.setId(2);
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user2);
        System.out.println(JSON.toJSON(list));
        return user;
    }

    public static void main(String[] args) {

        User user = new User();
        user.setName("asdasd");
        user.setPass("123a");
        user.setId(1);
        User user2 = new User();
        user2.setName("22222");
        user2.setPass("2");
        user2.setId(2);
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user2);
        System.out.println(JSON.toJSON(list));
    }
}
