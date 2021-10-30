package com.example.demo.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.utils.JacksonUtils;

@Component
@Aspect
@Order(0)																												// 指定AOP順序
public class MyRestAspect {
	
	@Pointcut("within(com.example.demo.controller..*)")
    public void pointcut() {
    }
	
	
	// method上有標註"@MyActionLog"的才會被縫進來
	@Around("pointcut()")
	public Object around1(ProceedingJoinPoint joinPoint) throws Throwable {
		
		System.out.println("MyRestAspect" + "*******");
		
		try {
			Object obj = joinPoint.proceed();
			return obj;
		}catch (Throwable e) {
			Map<String, Object> rsp = new HashMap<String, Object>();
			rsp.put("err-code", "E999");
			rsp.put("err-msg", e.getMessage());
			return JacksonUtils.bean2Json(rsp);
		}
		
		
	}
	
	
}
