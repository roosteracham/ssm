package com.zsf.util.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect // 声明方面， spring 自动探测
public class NotVeryUsefulAspect {

    @Pointcut("execution(public * com.zsf.business.AopTest.* (..))")
    private void anyOldTransfer() {

    }

    public void beforeTest() {
        System.out.println("before test");
    }

    @Before("anyOldTransfer()")
    public void afterSetTest() {
        System.out.println("after SetTest");
    }

    @Before("anyOldTransfer()")
    public void afterRtn(JoinPoint j) {
        System.out.println("afterRtn..");
        System.out.println(JSON.toJSON(j));
    }

    @Around("anyOldTransfer() && args(param)")
    public void AroundAdvicer(ProceedingJoinPoint pjp, String param) throws Throwable{
        System.out.println(param);
        System.out.println("before proceed()..");
        Object o = pjp.proceed(new Object[]{param + "modify"});
        System.out.println(JSON.toJSON(o));
        System.out.println("after proceed()..");
    }
}
