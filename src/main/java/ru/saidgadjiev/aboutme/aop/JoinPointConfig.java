package ru.saidgadjiev.aboutme.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by said on 02.08.2018.
 */
public class JoinPointConfig {

    @Pointcut("execution(* ru.saidgadjiev.aboutme.controller.*.*(..)))")
    public void logExecution() {}

    @Pointcut("execution(* ru.saidgadjiev.aboutme.controller.*.*(..)))")
    public void timeExecution() {}

}
