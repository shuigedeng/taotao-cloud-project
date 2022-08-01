package com.taotao.cloud.sys.biz.modules.classloader.dtos;

import lombok.Data;

/**
 * 类加载器加载的类信息
 */
@Data
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
}
