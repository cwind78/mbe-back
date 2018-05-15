package com.org.app.aop;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SessionChecker {
	@Around("within(@org.springframework.stereotype.Controller *)")
    public Object ctrlAspect(ProceedingJoinPoint jp) throws Throwable {
		HttpServletRequest req = null;
		HttpSession session = null;
		MethodSignature signature = (MethodSignature)jp.getSignature();
		String method = signature.getMethod().getName();
		String[] exceptMethodArray = {};//"loginCheck", "dupleCheck"
		
		/*
		 * < 0 이 맞지만 개발을 편하게 하기 위해 변경
		 * 
		 */
		/*if (Arrays.asList(exceptMethodArray).indexOf(method) > 0) {
			Object[] ob = jp.getArgs();
			for (Object obj : ob) {
				if (obj instanceof HttpServletRequest) {
					req = (HttpServletRequest)obj;
					session = req.getSession();
					if (session.getAttribute("USER_NAME") != null && !session.getAttribute("USER_NAME").equals("")) {*/
						return jp.proceed();
					/*} else {
						//loop
					}
				}
			}
		} else {
			System.out.println("Execute method : " + method);
			return jp.proceed();
		}
		
		return "/error/error";*/
    }
	
	@Around("within(@org.springframework.stereotype.Service *)")
    public Object serviceAspect(ProceedingJoinPoint jp) throws Throwable {
		return jp.proceed();
	}
}
