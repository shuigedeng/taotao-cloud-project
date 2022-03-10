package com.taotao.cloud.common.support.reflect.handler;

import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.support.reflect.simple.SimpleMethod;
import java.lang.reflect.Method;

/**
 * 方法处理类
 */
public class SimpleMethodHandler implements IHandler<Method, SimpleMethod> {

    @Override
    public SimpleMethod handle(Method method) {
        return null;
    }
}
