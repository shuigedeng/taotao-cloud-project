package com.taotao.cloud.sys.biz.tools.core.service.classloader.dtos;


import java.util.ArrayList;
import java.util.List;

/**
 * 类加载器加载的类信息
 */
public class LoadedClass {
    /**
     * 类全路径
     */
    private String className;
    /**
     * 字段数
     */
    private int fields;
    /**
     * 方法数
     */
    private int methods;

    public LoadedClass() {
    }

    public LoadedClass(String className, int fields, int methods) {
        this.className = className;
        this.fields = fields;
        this.methods = methods;
    }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getFields() {
		return fields;
	}

	public void setFields(int fields) {
		this.fields = fields;
	}

	public int getMethods() {
		return methods;
	}

	public void setMethods(int methods) {
		this.methods = methods;
	}
}
