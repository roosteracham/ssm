package com.zsf.controller;

import com.zsf.domain.ResBody;
import com.zsf.domain.UserInfo;
import com.zsf.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @RequestMapping("/register")
    public ResBody register(@RequestBody UserInfo user,
                            HttpServletRequest request, HttpServletResponse response) {
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
}
