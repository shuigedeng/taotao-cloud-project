package com.taotao.cloud.core.heaven.reflect.simple;


import com.taotao.cloud.core.heaven.reflect.api.IMethod;

/**
 * 简单方法
 */
public class SimpleMethod implements IMethod {

    private String name;

    private String fullName;

    private Class type;

    private int access;

    @Override
    public String name() {
        return name;
    }

    public SimpleMethod name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    public SimpleMethod fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public Class type() {
        return type;
    }

    public SimpleMethod type(Class type) {
        this.type = type;
        return this;
    }

    @Override
    public int access() {
        return access;
    }

    public SimpleMethod access(int access) {
        this.access = access;
        return this;
    }
}
