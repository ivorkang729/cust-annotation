package com.example.demo.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.anno.MyAuthRequired;

@Component
@Aspect
@Order(2)																												// 指定AOP順序
public class MyAuthAspect {
	
	private Map<String, List<String>> roleAuthSettings;
	
	@PostConstruct
	private void postConstruct() {
		roleAuthSettings = new HashMap<>();
		roleAuthSettings.put("ROLE_ADMIN", Arrays.asList("query", "delete"));
		roleAuthSettings.put("ROLE_USER", Arrays.asList("query"));
	}
	
	@Pointcut("within(com.example.demo.controller..*)")
    public void pointcut() {
    }
	
	
	// method上有標註"@MyAuthRequired"的才會被縫進來
	@Around("pointcut() && @annotation(myAnno)")
	public Object around1(ProceedingJoinPoint joinPoint, MyAuthRequired myAnno) throws Throwable {
		
		// ProceedingJoinPoint 的簡單用法，可參考這文件：
		// https://blog.csdn.net/qq_36084681/article/details/80447113
		
		System.out.println("MyAuthAspect" + "++++++++");
		System.out.println("myAnno = " + myAnno + " ++++++++");
		
		// 登入者有什麼角色
		String role = joinPoint.getArgs()[0].toString();
		
		// 這方法需要什麼權限
		String authId = myAnno.authId();
		
		// 登入者擁有的角色是否有需要的權限
		if (Optional.ofNullable(roleAuthSettings.get(role)).orElse(new ArrayList<>()).contains(authId)) {
			// 有權限
			Object obj = joinPoint.proceed();
			return obj;
		}
		else {
			// 無權限
			throw new RuntimeException("無權限");
		}
		
		
	}
	
	
	private void checkAuthRequired(ProceedingJoinPoint joinPoint) {
		
		// JoinPoint Method
		System.out.println("JoinPoint Method --------------------");
		System.out.println("joinPoint.getSignature().getName() = " + joinPoint.getSignature().getName());
		System.out.println("joinPoint.getSignature().getDeclaringTypeName() = " + joinPoint.getSignature().getDeclaringTypeName());
		System.out.println("joinPoint.getSignature().getModifiers() = " + joinPoint.getSignature().getModifiers());
		System.out.println();
		
		// JoinPoint Arguments
		System.out.println("JoinPoint Arguments --------------------");
		System.out.println("joinPoint.getArgs() = " + joinPoint.getArgs());
		System.out.println("joinPoint.getArgs().length = " + joinPoint.getArgs().length);
		for (int i = 0; i < joinPoint.getArgs().length; i++) {
			System.out.println(String.format("joinPoint.getArgs()[%s].getClass().getName() = %s", i, joinPoint.getArgs()[i].getClass().getName()));
			System.out.println(String.format("joinPoint.getArgs()[%s].toString() = %s", i, joinPoint.getArgs()[i].toString()));
		}
		System.out.println();

		// JoinPoint Target Object's Class
		System.out.println("JoinPoint Target Object's Class --------------------");
		System.out.println("joinPoint.getTarget() = " + joinPoint.getTarget());
		System.out.println("joinPoint.getTarget().getClass() = " + joinPoint.getTarget().getClass());
		System.out.println("joinPoint.getTarget().getClass().getName() = " + joinPoint.getTarget().getClass().getName());
		System.out.println("joinPoint.getTarget().getClass().getSimpleName() = " + joinPoint.getTarget().getClass().getSimpleName());
		System.out.println();

		//
		try {
			
			System.out.println("檢查這個Method有沒有我想找的Annotation --------------------");
			String methodName = joinPoint.getSignature().getName();
			List<Class<?>> parameterList = Arrays.asList(joinPoint.getArgs()).stream().map(Object::getClass).collect(Collectors.toList());
			Class<?>[] parameterTypes = parameterList.toArray(new Class[parameterList.size()]);
			System.out.println("method = " + methodName);
			System.out.println("parameterTypes = " + parameterTypes);
			
			Method method = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
			System.out.println("method.isAnnotationPresent(MyAuthRequired.class) = " + method.isAnnotationPresent(MyAuthRequired.class));
			System.out.println("method.getDeclaredAnnotation(MyAuthRequired.class) = " + method.getDeclaredAnnotation(MyAuthRequired.class));				// 回傳一筆，適用於 Non-repeatable Annotation。如果此Method無套用預查找的Annotation，則回傳null
			System.out.println("method.getDeclaredAnnotationsByType(MyAuthRequired.class) = " + method.getDeclaredAnnotationsByType(MyAuthRequired.class));	// 回傳[]，適用於 Repeatable Annotation。如果此Method無套用預查找的Annotation，則回傳[0]
			
			// 這個Method有我想找的Annotation
			MyAuthRequired myAuthRequiredAnnotation = method.getDeclaredAnnotation(MyAuthRequired.class);
			if (myAuthRequiredAnnotation != null) {
				String authId = myAuthRequiredAnnotation.authId();
				System.out.println(String.format("需檢查登入者是否有%s權限", authId));
				
				// 模擬無權限
				//throw new RuntimeException("E403-無權限");
			}
			else {
				System.out.println("此Method無任何MyAuthRequired Annotation");
			}
			
			
			
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		
		
		
//		System.out.println(" = " + );
		
		
		
		
	}
	

}
