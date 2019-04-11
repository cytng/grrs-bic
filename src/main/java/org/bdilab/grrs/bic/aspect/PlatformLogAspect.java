package org.bdilab.grrs.bic.aspect;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.bdilab.grrs.bic.param.LoggerName;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Aspect
@Component
public class PlatformLogAspect {

    @Pointcut(value = "execution(public * org.bdilab.grrs.bic.service.*.*(..)) || execution(public * AdminIdentityAspect.identitfy(..))")
    public void platformOps(){}

    @Around("platformOps()")
    public Object writeLog(ProceedingJoinPoint joinPoint) {
        Method targetMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = targetMethod.getName();
        Object[] args = joinPoint.getArgs();
        try {
            LogManager.getLogger(LoggerName.PLATFORM).trace("Start Ops[{}]", methodName);
            ResponseEntity entity = (ResponseEntity) joinPoint.proceed();
            LogManager.getLogger(LoggerName.PLATFORM).trace("Ops[{}] End", methodName);
            if (HttpStatus.OK.equals(entity.getStatusCode())) {
                LogManager.getLogger(LoggerName.PLATFORM).info("Ops[{}] success with params[{}]", methodName, args);
            } else {
                LogManager.getLogger(LoggerName.PLATFORM).info("Ops[{}] failed with params[{}]", methodName, args);
            }
            return entity;
        } catch (Throwable throwable) {
            LogManager.getLogger(LoggerName.PLATFORM).warn( "Ops[" + methodName + "] failed with exceptions", throwable);
            return new ResponseEntity<>(throwable, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
