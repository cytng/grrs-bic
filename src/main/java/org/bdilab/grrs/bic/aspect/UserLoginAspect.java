package org.bdilab.grrs.bic.aspect;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.bdilab.grrs.bic.param.LoggerName;
import org.bdilab.grrs.bic.util.CommonUtil;
import org.bdilab.grrs.bic.util.ResponseResultUtil;
import org.bdilab.grrs.bic.util.UserUtil;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @author caytng@163.com
 * @date 2019/4/13
 */
@Aspect
@Component
@Order(1)
public class UserLoginAspect {

    @AfterReturning(returning = "result", pointcut = "execution(public * org.bdilab.grrs.bic.controller.UserController.login(..))")
    public void recordLoginStatus(ResponseEntity result) {
        if (result.getBody() instanceof UserInfo) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            session.setAttribute(UserUtil.CUR_USER, result.getBody());
        }
    }

    @Pointcut(value =  "(!execution(public * org.bdilab.grrs.bic.controller.UserController.login(..)) " +
            "&& execution(public * org.bdilab.grrs.bic.controller.*.*(..)))")
    public void loginedOps(){}

    @Around("loginedOps()")
    public Object checkLoginStatus(ProceedingJoinPoint joinPoint) throws Throwable {
        Method targetMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = targetMethod.getName();
        LogManager.getLogger(LoggerName.PLATFORM).trace("Prepare Ops[{}]", methodName);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        UserInfo curUser = (UserInfo) session.getAttribute(UserUtil.CUR_USER);
        if (CommonUtil.isNull(curUser)) {
            return ResponseResultUtil.missingSession();
        }
        Object[] args = joinPoint.getArgs();
        args[0] = curUser;
        return joinPoint.proceed(args);
    }
}
