package com.taotao.cloud.sys.biz.api.controller.tools.core.aspect;

import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class SerializerAspect {

    @Pointcut("@annotation(com.sanri.tools.modules.core.aspect.SerializerToFile)")
    public void pointcut(){}

    @AfterReturning("pointcut()")
    public void afterReturn(JoinPoint joinPoint){
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Class<?> declaringType = signature.getDeclaringType();
        final Method serializer = MethodUtils.getAccessibleMethod(declaringType, "serializer");
        if (serializer != null) {
            ReflectionUtils.invokeMethod(serializer, joinPoint.getTarget());
        }
    }
}
