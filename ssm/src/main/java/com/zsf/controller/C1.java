package com.zsf.controller;

import com.zsf.domain.User;
import com.zsf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/c1")
public class C1 {

    @Autowired
    private IUserService userService;

    @RequestMapping("/user/{id}")
    public String c(@PathVariable("id") int id) {
        User user = userService.selectByPrimaryKey(id);
        System.out.println(user.getName() + " : " + user.getPass());
        return  null;
    }
}
