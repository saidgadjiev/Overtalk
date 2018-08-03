package ru.saidgadjiev.aboutme.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

//@Aspect
//@Configuration
public class LogAspect {

    private static final Logger LOGGER = Logger.getLogger(LogAspect.class);

    @Before("ru.saidgadjiev.aboutme.aop.JoinPointConfig.logExecution()")
    public void before(JoinPoint joinPoint) {
        LOGGER.debug(String.format(" Allowed execution for %s", joinPoint));
    }

    @AfterReturning(value = "ru.saidgadjiev.aboutme.aop.JoinPointConfig.logExecution()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.debug(String.format("%s returned with value %s", joinPoint, result));
    }
}
