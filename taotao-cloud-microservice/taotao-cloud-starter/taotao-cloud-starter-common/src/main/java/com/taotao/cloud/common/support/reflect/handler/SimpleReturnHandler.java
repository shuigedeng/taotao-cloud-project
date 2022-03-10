package com.taotao.cloud.common.support.reflect.handler;


import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.support.reflect.simple.SimpleReturn;

/**
 * 返回值处理类
 */
public class SimpleReturnHandler implements IHandler<Class, SimpleReturn> {

    @Override
    public SimpleReturn handle(Class aClass) {
        return null;
    }

}
