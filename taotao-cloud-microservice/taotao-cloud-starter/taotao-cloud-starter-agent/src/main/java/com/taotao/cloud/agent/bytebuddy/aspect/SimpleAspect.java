package com.taotao.cloud.agent.bytebuddy.aspect;

import java.lang.reflect.Method;

/**
 * Created by pphh on 2022/6/24.
 */
public class SimpleAspect implements IAspect {

    @Override
    public void beforeMethod(Object objInst, Method method, Object[] allArguments, Object result) throws Throwable {
        System.out.println("start of method()");
    }

    @Override
    public Object afterMethod(Object objInst, Method method, Object[] allArguments, Object result) throws Throwable {
        System.out.println("end of method()");
        return result;
    }

    @Override
    public void handleMethodException(Object objInst, Method method, Object[] allArguments, Throwable t) {
        System.out.println("an exception thrown from method()");
    }

}
