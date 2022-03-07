package com.taotao.cloud.core.heaven.reflect.simple;


import com.taotao.cloud.core.heaven.reflect.api.IParam;

/**
 * 简单参数
 */
public class SimpleParam implements IParam {

    private String name;

    private String fullName;

    private Class type;

    private int access;

    @Override
    public String name() {
        return name;
    }

    public SimpleParam name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    public SimpleParam fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public Class type() {
        return type;
    }

    public SimpleParam type(Class type) {
        this.type = type;
        return this;
    }

    @Override
    public int access() {
        return access;
    }

    public SimpleParam access(int access) {
        this.access = access;
        return this;
    }
}
