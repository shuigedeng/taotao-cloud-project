package com.taotao.cloud.agent.bytebuddy.aspect;

import java.lang.reflect.Method;

/**
 * Created by pphh on 2022/6/24.
 */
public interface IAspect {

    /**
     * called before target method invocation.
     *
     * @param result change this result, if you want to truncate the method.
     */
    void beforeMethod(Object objInst, Method method, Object[] allArguments, Object result) throws Throwable;

    /**
     * called after target method invocation. Even method's invocation triggers an exception.
     * i
     *
     * @param result the method's orignal return value. May be null if the method triggers an exception.
     * @return the method's actual return value.
     */
    Object afterMethod(Object objInst, Method method, Object[] allArguments, Object result) throws Throwable;

    /**
     * called when occur exception.
     *
     * @param t the exception occur.
     */
    void handleMethodException(Object objInst, Method method, Object[] allArguments, Throwable t);

}
