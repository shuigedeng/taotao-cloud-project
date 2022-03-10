package com.taotao.cloud.common.support.reflect.simple;


import com.taotao.cloud.common.support.reflect.api.IClass;
import com.taotao.cloud.common.support.reflect.api.IField;
import com.taotao.cloud.common.support.reflect.api.IMethod;
import java.util.List;

/**
 * 简单类
 */
public class SimpleClass implements IClass {

    @Override
    public List<IField> fields() {
        return null;
    }

    @Override
    public List<IMethod> methods() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String fullName() {
        return null;
    }

    @Override
    public Class type() {
        return null;
    }

    @Override
    public int access() {
        return 0;
    }
}
