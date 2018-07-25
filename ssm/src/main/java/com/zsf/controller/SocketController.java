package com.zsf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/ws")
public class SocketController {

    @RequestMapping("/test")
    public void test(HttpServletResponse response) throws Exception{
        response.getWriter().write("asdasda");
    }
}
