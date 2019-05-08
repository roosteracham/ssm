package com.zsf.controller;

import com.alibaba.fastjson.JSON;
import com.zsf.domain.*;
import com.zsf.service.IUserService;
import com.zsf.service.RedisService;
import com.zsf.util.encode.Param;
import com.zsf.util.errorcode.RedirectEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @RequestMapping("/register")
    public ResBody register(@RequestBody UserInfo user) {
        return userService.register(user);
    }


    @RequestMapping(value = "/confirm/{param}", method = RequestMethod.GET)
    public void confirm(@PathVariable("param") String param,
                        HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(userService.confirm(param, request));
        } catch (IOException e) {
            logger.error("【 " + Thread.currentThread().getName() +
                    "】 重定向出错", e.getMessage());
        }
    }

    @RequestMapping("/login")
    public ResBody login(@RequestBody UserInfo user,
                         HttpServletResponse response) {
        return userService.login(user, response);
    }

    @RequestMapping(value = "/roleManage", method = RequestMethod.POST)
    public ResBody roleManage(@RequestBody UserSvgsDto userSvgsDto) {
        return userService.roleManage(userSvgsDto);
    }

    @RequestMapping(value = "/userManager", method = RequestMethod.POST)
    public String userManager(@RequestBody UserInfo user, HttpServletRequest request,
                              HttpServletResponse response, HttpSession session) {
        return userService.userManager(user, request, session);
    }
}
