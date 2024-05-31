package com.apivideo.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final ObjectMapper objectMapper;

    public LoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(* com.apivideo.controller..*(..))")
    public void controllerMethods() {
        // 切点，包含所有方法    
    }

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 记录方法调用和参数
        String methodName = joinPoint.getSignature().toShortString();
        String args = objectMapper.writeValueAsString(joinPoint.getArgs());
        logger.info("Method {} called with arguments: {}", methodName, args);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Exception in {}: {}", methodName, e.getMessage());
            throw e;
        }

        // 记录方法返回值和执行时间
        long elapsedTime = System.currentTimeMillis() - startTime;
        String returnValue = objectMapper.writeValueAsString(result);
        logger.info("Method {} returned {} in {} ms", methodName, returnValue, elapsedTime);

        return result;
    }
}
