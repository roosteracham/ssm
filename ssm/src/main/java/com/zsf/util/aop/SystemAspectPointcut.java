package com.zsf.util.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemAspectPointcut {

    @Pointcut("within(com.zsf.business..*)")
    public void setAopTest() {}
}
