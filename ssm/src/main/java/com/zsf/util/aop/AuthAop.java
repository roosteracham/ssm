package com.zsf.util.aop;

import com.alibaba.fastjson.JSON;
import com.zsf.domain.ResBody;
import com.zsf.domain.TokenLocation;
import com.zsf.service.RedisService;
import com.zsf.util.errorcode.ErrorCodeEnum;
import com.zsf.util.errorcode.RedirectEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

        String token = request.getHeader("Authorization");
        boolean failed = true;
        if (token != null) {
            token = token.split(" ")[1];
            String value = redisService.getValue(token);

            // 如果value不空，通过身份验证
            if (value != null) {

                try {
                    // 获取函数返回值， 并设置请求返回值
                    ResBody resBody = (ResBody) joinPoint.proceed(args);
                    HttpServletResponse response = (HttpServletResponse) args[2];
                    response.setHeader("Content-Type",
                            "application/json; charset=utf-8");
                    response.getWriter().append(JSON.toJSONString(resBody));
                    // 重新设置身份验证有效时间
                    redisService.setExpired(token, value, 60, TimeUnit.MINUTES);

                    failed = false;
                }catch (Throwable e) {
                    logger.error("【 " + Thread.currentThread().getName() +
                            "】 无法进入controller", e.getMessage());
                }
            }
        }

        if (failed) {
            authFailed(args[2]);
        }

    }

    private void authFailed(Object arg) {
        // 跳转到登陆
        HttpServletResponse response = (HttpServletResponse) arg;
        response.setHeader("Content-Type",
                "application/json; charset=utf-8");
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
