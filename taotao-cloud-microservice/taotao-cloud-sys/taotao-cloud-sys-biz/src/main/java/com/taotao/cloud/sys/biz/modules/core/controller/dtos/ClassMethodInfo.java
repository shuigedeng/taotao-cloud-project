package com.taotao.cloud.sys.biz.modules.core.controller.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个简单的方法信息
 */
@Data
public class ClassMethodInfo {
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数列表
     */
    private List<Arg> args = new ArrayList<>();
    /**
     * 返回类型
     */
    private JavaType returnType;

    public ClassMethodInfo() {
    }

    public ClassMethodInfo(String methodName, List<Arg> args, JavaType returnType) {
        this.methodName = methodName;
        this.args = args;
        this.returnType = returnType;
    }

    public static final class Arg{
        /**
         * 参数类型
         */
        private JavaType type;
        /**
         * 参数名称
         */
        private String name;

        public Arg() {
        }

        public Arg(JavaType type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    @Data
    public static final class JavaType{
        /**
         * 类型名称, 全路径
         */
        private String name;
        /**
         * 简单类型名称
         */
        private String simpleName;

        public JavaType() {
        }

        public JavaType(String name, String simpleName) {
            this.name = name;
            this.simpleName = simpleName;
        }
    }
}
