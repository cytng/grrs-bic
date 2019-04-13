package org.bdilab.grrs.bic.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.bdilab.grrs.bic.entity.UserInfo;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
            session.setAttribute("curUser", result.getBody());
        }
    }
}
