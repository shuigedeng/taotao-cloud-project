package com.taotao.cloud.core.heaven.reflect.handler;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.reflect.simple.SimpleClass;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import java.lang.annotation.Annotation;

/**
 * 类处理类
 */
@ThreadSafe
public class SimpleClassHandler implements IHandler<Class, SimpleClass> {

    @Override
    public SimpleClass handle(Class aClass) {
        return null;
    }

}
