package com.taotao.cloud.rpc.common.api;

import com.taotao.cloud.rpc.common.api.ProxyFactory;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.invoke.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK 动态代理工厂实现
 * <p>
 * ProxyFactory SPI 的默认实现，用于消费端创建接口代理。
 */
public class JdkProxyFactory implements ProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker) {
        return getProxy(invoker, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, boolean generic) {
        if (invoker == null) {
            throw new IllegalArgumentException("invoker == null");
        }
        Class<?> iface = invoker.getInterface();
        if (!iface.isInterface()) {
            throw new IllegalStateException("Interface " + iface.getName() + " is not an interface");
        }
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, java.net.URL url) {
        // 简化实现：返回基于反射的 Invoker
        return new ReflectiveInvoker<>(proxy, type, url == null ? null : new URL(url.getProtocol(), url.getHost(), url.getPort()));
    }

    /**
     * 调用处理器
     */
    private static class InvokerInvocationHandler implements InvocationHandler {
        private final Invoker<?> invoker;

        InvokerInvocationHandler(Invoker<?> invoker) {
            this.invoker = invoker;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?>[] paramTypes = method.getParameterTypes();

            // 处理 Object 方法
            if ("toString".equals(methodName) && paramTypes.length == 0) {
                return invoker.toString();
            }
            if ("hashCode".equals(methodName) && paramTypes.length == 0) {
                return invoker.hashCode();
            }
            // 只认领 Object 的那三个方法：接口自己声明 equals(String) 之类的重载时，
            // 那是业务方法，按名字拦截会让远程调用静默变成一个布尔常量。
            if ("equals".equals(methodName) && paramTypes.length == 1 && paramTypes[0] == Object.class) {
                return proxy == args[0];
            }

            RpcInvocation invocation = new RpcInvocation();
            invocation.setMethodName(methodName);
            invocation.setParameterTypes(paramTypes);
            invocation.setArguments(args != null ? args : new Object[0]);
            if (invoker.getUrl() != null) {
                invocation.setInvokerUrl(invoker.getUrl());
            }
            if (invoker.getInterface() != null) {
                invocation.setServiceInterface(invoker.getInterface().getName());
            }

            Result r = invoker.invoke(invocation);
            if (r.hasException()) throw r.getException();
            return r.getValue();
        }
    }

    /**
     * 反射调用的 Invoker（用于服务端）
     */
    private static class ReflectiveInvoker<T> implements Invoker<T> {
        private final T proxy;
        private final Class<T> type;
        private final URL url;

        ReflectiveInvoker(T proxy, Class<T> type, URL url) {
            this.proxy = proxy;
            this.type = type;
            this.url = url;
        }

        @Override
        public Class<T> getInterface() {
            return type;
        }

        @Override
        public Result invoke(Invocation invocation) throws Throwable {
            try {
                Method method = type.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                Object value = method.invoke(proxy, invocation.getArguments());
                return new Result.RpcResult(value);
            } catch (java.lang.reflect.InvocationTargetException ite) {
                return new Result.RpcResult(ite.getTargetException());
            } catch (Throwable t) {
                return new Result.RpcResult(t);
            }
        }

        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public boolean isAvailable() {
            return proxy != null;
        }

        @Override
        public void destroy() {
        }
    }
}
