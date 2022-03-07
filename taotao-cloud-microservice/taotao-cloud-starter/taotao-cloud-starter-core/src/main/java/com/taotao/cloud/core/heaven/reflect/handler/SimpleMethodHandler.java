package com.taotao.cloud.core.heaven.reflect.handler;

import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.reflect.simple.SimpleMethod;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 方法处理类
 */
@ThreadSafe
public class SimpleMethodHandler implements IHandler<Method, SimpleMethod> {

    @Override
    public SimpleMethod handle(Method method) {
        return null;
    }
}
