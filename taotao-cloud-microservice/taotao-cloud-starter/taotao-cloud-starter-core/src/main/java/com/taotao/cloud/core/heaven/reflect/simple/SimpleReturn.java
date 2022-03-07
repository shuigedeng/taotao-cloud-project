package com.taotao.cloud.core.heaven.reflect.simple;


import com.taotao.cloud.core.heaven.reflect.api.IReturn;

/**
 * 简单返回值
 */
public class SimpleReturn implements IReturn {

    private String name;

    private String fullName;

    private Class type;

    private int access;

    @Override
    public String name() {
        return name;
    }

    public SimpleReturn name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    public SimpleReturn fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public Class type() {
        return type;
    }

    public SimpleReturn type(Class type) {
        this.type = type;
        return this;
    }

    @Override
    public int access() {
        return access;
    }

    public SimpleReturn access(int access) {
        this.access = access;
        return this;
    }
}
