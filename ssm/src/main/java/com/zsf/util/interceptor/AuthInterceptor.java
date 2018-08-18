package com.zsf.util.interceptor;

import com.alibaba.fastjson.JSON;
import com.zsf.domain.ResBody;
import com.zsf.domain.TokenLocation;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import com.zsf.util.errorcode.RedirectEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        System.out.println(Thread.currentThread().getName());
        Cookie[] cookies = request.getCookies();
        String token = request.getHeader("Authorization");
        if (token != null) {
            token = token.split(" ")[1];
            String value = redisService.getValue(token);
            //String v = redisDao.getValue("工程名称_工程画面_0");
            if (value != null) {
                request.setAttribute("token", token);
                request.setAttribute("value", value);
                return true;
            }
        }

        // 跳转到登陆
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        ResBody resBody = new ResBody();
        resBody.setSuccess(false);
        resBody.setErrorCode(ErrorCodeEnum.FAIL.getIndex());
        TokenLocation tokenLocation = new TokenLocation();
        tokenLocation.setLocation(RedirectEnum.LOGIN);
        tokenLocation.setToken("login");
        resBody.setData(JSON.toJSONString(tokenLocation));
        response.getWriter().append(JSON.toJSONString(resBody));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String token = (String)request.getAttribute("token");
        String value = (String)request.getAttribute("value");
        redisService.setExpired(token, value, 60, TimeUnit.MINUTES);
    }
}
