package com.taotao.cloud.common.support.reflect.simple;


import com.taotao.cloud.common.support.reflect.api.IAnnotation;

/**
 * 注解实现
 */
public class SimpleAnnotation implements IAnnotation {

    private String name;

    private String fullName;

    private Class type;

    private int access;

    @Override
    public String name() {
        return name;
    }

    public SimpleAnnotation name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    public SimpleAnnotation fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public Class type() {
        return type;
    }

    public SimpleAnnotation type(Class type) {
        this.type = type;
        return this;
    }

    @Override
    public int access() {
        return access;
    }

    public SimpleAnnotation access(int access) {
        this.access = access;
        return this;
    }
}
