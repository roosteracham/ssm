package com.zsf.util.aop;

import com.alibaba.fastjson.JSON;
import com.zsf.domain.ResBody;
import com.zsf.domain.TokenLocation;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import com.zsf.util.errorcode.RedirectEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Aspect
public class AuthAop {

    private Logger logger = LoggerFactory.getLogger(AuthAop.class);

    @Autowired
    private RedisService redisService;


    @Pointcut("execution(public * com.zsf.controller.ProjectController.* (..))")
    private void projectAuth() {

    }

    @Around("projectAuth()")
    public void auth(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[1];
        System.out.println(Thread.currentThread().getName());

        String token = request.getHeader("Authorization");
        if (token != null) {
            token = token.split(" ")[1];
            String value = redisService.getValue(token);
            //String v = redisDao.getValue("工程名称_工程画面_0");
            if (value != null) {
                request.setAttribute("token", token);
                request.setAttribute("value", value);
                try {
                    ResBody resBody = (ResBody) joinPoint.proceed(args);
                    HttpServletResponse response = (HttpServletResponse) args[2];
                    response.setHeader("Content-Type", "application/json; charset=utf-8");
                    response.getWriter().append(JSON.toJSONString(resBody));

                }catch (Throwable e) {
                    logger.error("【 " + Thread.currentThread().getName() +
                            "】 无法进入controller", e.getMessage());
                }
            } else {
                authFailed(args[2]);
            }
        }else {
            authFailed(args[2]);
        }


    }

    private void authFailed(Object arg) {
        // 跳转到登陆
        HttpServletResponse response = (HttpServletResponse) arg;
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        ResBody resBody = new ResBody();
        resBody.setSuccess(false);
        resBody.setErrorCode(ErrorCodeEnum.FAIL.getIndex());
        TokenLocation tokenLocation = new TokenLocation();
        tokenLocation.setLocation(RedirectEnum.LOGIN);
        tokenLocation.setToken("login");
        resBody.setData(JSON.toJSONString(tokenLocation));
        try {
            response.getWriter().append(JSON.toJSONString(resBody));
        } catch (IOException e) {
            logger.error("【 " + Thread.currentThread().getName() +
                    "】 重定向出错", e.getMessage());
        }
    }
}
