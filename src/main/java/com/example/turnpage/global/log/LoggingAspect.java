package com.example.turnpage.global.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(*com.example.turnpage.domain..*)")
    private void domain() {
    }

    @Around("domain()")
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethod(joinPoint);

        long startTime = System.currentTimeMillis();  // 시작 시간 기록

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n=== START: %s, %s%n",
                method.getDeclaringClass().getSimpleName(),
                method.getName()));

        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                sb.append("PARAM => ");
                sb.append(arg).append("\n");
            } else {
                sb.append("NO PARAM\n");
            }
        }


        Object returnObj = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - startTime;
        sb.append(String.format("RESULT: %s%n", returnObj.getClass().getSimpleName()));
        sb.append(String.format("EXECUTION TIME: %d ms ===", executionTime));

        log.info(sb.toString());
        return returnObj;
    }


    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        return signature.getMethod();
    }
}
