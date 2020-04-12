package org.zsf.service.impl;

import org.zsf.service.GreetingService;

public class GreetingServiceImpl implements GreetingService {
    @Override
    public String sayHi(String name) {
        return "hi: " + name;
    }
}
