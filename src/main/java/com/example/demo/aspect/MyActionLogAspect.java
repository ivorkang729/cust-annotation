package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.anno.MyActionLog;

@Component
@Aspect
@Order(1)																												// 指定AOP順序
public class MyActionLogAspect {
	
	@Pointcut("within(com.example.demo.controller..*)")
    public void pointcut() {
    }
	
	
	// method上有標註"@MyActionLog"的才會被縫進來
	@Around("pointcut() && @annotation(myAnno)")
	public Object around1(ProceedingJoinPoint joinPoint, MyActionLog myAnno) throws Throwable {
		
		System.out.println("MyActionLogAspect" + "*******");
		System.out.println("myAnno = " + myAnno + " ++++++++");
		
		try {
			Object obj = joinPoint.proceed();
			System.out.println(String.format("紀錄ActionLog ==> \"action\":%s, \"result\":%s", myAnno.action(), "SUCCESS"));
			return obj;
		}catch (Throwable e) {
			System.out.println(String.format("紀錄ActionLog ==> \"action\":%s, \"result\":%s", myAnno.action(), e.getMessage()));
			throw e;
		}
		
		
	}
	
	
}
