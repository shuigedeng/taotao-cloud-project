package com.taotao.cloud.common.support.proxy;

import java.lang.reflect.Method;

/**
 * 方法的处理
 */
public interface IMethodHandler {

    /**
     * 方法的处理
     * @param proxy 代理类
     * @param method  方法
     * @param args 参数
     * @return 结果
     * @throws Throwable if any
     */
    Object handle(final Object proxy,
                  final Method method,
                  final Object[] args) throws Throwable;

}
