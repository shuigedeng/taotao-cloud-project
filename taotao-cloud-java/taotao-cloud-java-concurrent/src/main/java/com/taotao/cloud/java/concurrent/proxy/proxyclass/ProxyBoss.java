package com.taotao.cloud.java.proxy.proxyclass;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyBoss {
    /**
     * 对接口方法进行代理
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(final int discountCoupon, final Class<?> interfaceClass, final Class<?> implementsClass) throws Exception {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Integer returnValue = (Integer) method.invoke(implementsClass.newInstance(), args);// 调用原始对象以后返回的值
                return returnValue - discountCoupon;
            }
        });
    }
}
