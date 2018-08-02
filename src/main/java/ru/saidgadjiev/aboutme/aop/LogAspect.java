package ru.saidgadjiev.aboutme.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LogAspect {

    private static final Logger LOGGER = Logger.getLogger(LogAspect.class);

    @Before("execution(* ru.saidgadjiev.aboutme.controller.*.*(..)))")
    public void before(JoinPoint joinPoint) {
        LOGGER.debug(joinPoint);
    }

    @AfterReturning(value = "execution(* ru.saidgadjiev.aboutme.controller.*.*(..)))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.info("{} returned with value {}", joinPoint, result);
    }
}
