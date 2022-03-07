package com.taotao.cloud.core.heaven.reflect.handler;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.reflect.simple.SimpleField;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 字段处理类
 */
@ThreadSafe
public class SimpleFieldHandler implements IHandler<Field, SimpleField> {

    @Override
    public SimpleField handle(Field field) {
        SimpleField simpleField = new SimpleField();
        simpleField.field(field);
        simpleField.name(field.getName());
        simpleField.fullName(field.getName());
        simpleField.type(field.getType());
        simpleField.annotations(Arrays.asList(field.getAnnotations()));
        simpleField.access(field.getModifiers());
        return simpleField;
    }

}
