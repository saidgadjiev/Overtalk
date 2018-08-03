package ru.saidgadjiev.aboutme.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;


/**
 * Created by said on 02.08.2018.
 */
//@Aspect
//@Configuration
public class TimeAspect {

    private static final Logger LOGGER = Logger.getLogger(TimeAspect.class);

    @Around("ru.saidgadjiev.aboutme.aop.JoinPointConfig.timeExecution()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long timeTaken = System.currentTimeMillis() - startTime;

        LOGGER.debug(String.format("Time Taken by %s is %s", joinPoint, timeTaken));

        return result;
    }
}
