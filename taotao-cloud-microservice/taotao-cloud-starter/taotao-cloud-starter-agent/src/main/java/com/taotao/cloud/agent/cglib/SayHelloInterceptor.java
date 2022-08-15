package com.taotao.cloud.agent.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by pphh on 2022/6/24.
 */
public class SayHelloInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] params, MethodProxy proxy) throws Throwable {
        System.out.println("begin of sayHello(), proxy class = " + obj.getClass().getName());
        Object result = proxy.invokeSuper(obj, params);
        System.out.println("end of sayHello(), result = " + result);
        return result;
    }
}
