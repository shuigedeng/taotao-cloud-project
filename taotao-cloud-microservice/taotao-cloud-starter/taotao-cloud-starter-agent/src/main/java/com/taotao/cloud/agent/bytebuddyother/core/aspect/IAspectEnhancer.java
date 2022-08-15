package com.taotao.cloud.agent.bytebuddyother.core.aspect;

import java.lang.reflect.Method;

/**
 * Created by pphh on 2022/8/4.
 * 类切面增强定义
 */
public interface IAspectEnhancer {

    /**
     * 在目标方法执行前调用
     * called before target method invocation.
     *
     * @param objInst
     * @param method
     * @param allArguments
     * @param argumentsTypes
     * @param result         change this result, if you want to truncate the method.
     * @throws Throwable
     */
    void beforeMethod(Object objInst,
                      Method method,
                      Object[] allArguments,
                      Class<?>[] argumentsTypes,
                      Object result) throws Throwable;

    /**
     * 在目标方法执行后调用
     * called after target method invocation. Even method's invocation triggers an exception.
     *
     * @param objInst
     * @param method
     * @param allArguments
     * @param argumentsTypes
     * @param ret            the method's orignal return value. May be null if the method triggers an exception.
     * @return the method's actual return value.
     * @throws Throwable
     */
    Object afterMethod(Object objInst,
                       Method method,
                       Object[] allArguments,
                       Class<?>[] argumentsTypes,
                       Object ret) throws Throwable;


    /**
     * 在目标方法有异常时调用
     * called when occur exception.
     *
     * @param objInst
     * @param method
     * @param allArguments
     * @param argumentsTypes
     * @param t              the exception occur.
     */
    void handleMethodException(Object objInst,
                               Method method,
                               Object[] allArguments,
                               Class<?>[] argumentsTypes,
                               Throwable t);
}
