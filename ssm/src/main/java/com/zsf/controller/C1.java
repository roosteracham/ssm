package com.zsf.controller;

import com.zsf.domain.ResultBean;
import com.zsf.domain.User;
import com.zsf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


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

    @RequestMapping("/a")
    public String a() {
        return "a";
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/test-cross", method = RequestMethod.POST)
    public @ResponseBody ResultBean test(HttpServletResponse response) {
        System.out.println("request received.");
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        return new ResultBean("returned meaasge id : " + (int)(Math.random() * 100));
    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public ResultBean test1() {
        System.out.println("request received.");
        return new ResultBean("returned meaasge id : " + (int)(Math.random() * 100));
    }
}
