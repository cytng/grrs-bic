package org.bdilab.grrs.bic.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.bdilab.grrs.bic.util.ResponseResultUtil;
import org.bdilab.grrs.bic.util.UserUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
@Aspect
@Component
@Order(1)
public class AdminIdentityAspect {

    @Pointcut(value = "execution(public * org.bdilab.grrs.bic.controller.AdminController.*(..))")
    public void adminOps(){}

    @Around("adminOps()")
    public Object identitfy(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("curUser");
        if (UserUtil.isNotAdmin(userInfo)) {
            return ResponseResultUtil.withoutPermmision();
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            return ResponseResultUtil.internalError(throwable);
        }
    }
}
