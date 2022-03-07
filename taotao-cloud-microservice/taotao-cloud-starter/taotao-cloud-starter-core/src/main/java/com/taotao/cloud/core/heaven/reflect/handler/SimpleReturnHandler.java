package com.taotao.cloud.core.heaven.reflect.handler;

import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.reflect.simple.SimpleReturn;
import com.taotao.cloud.core.heaven.support.handler.IHandler;

/**
 * 返回值处理类
 */
@ThreadSafe
public class SimpleReturnHandler implements IHandler<Class, SimpleReturn> {

    @Override
    public SimpleReturn handle(Class aClass) {
        return null;
    }

}
