package com.taotao.cloud.core.heaven.reflect.handler;

import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.reflect.simple.SimpleParam;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import java.lang.reflect.Method;

/**
 * 参数处理类
 */
@ThreadSafe
public class SimpleParamHandler implements IHandler<Class, SimpleParam> {

    @Override
    public SimpleParam handle(Class aClass) {
        return null;
    }

}
