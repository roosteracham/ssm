package com.zsf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ws")
public class SocketController {

    @RequestMapping("/test")
    public String test() {
        return "testws";
    }
}
