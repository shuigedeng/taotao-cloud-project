package com.taotao.cloud.open.openapi.model;

import com.taotao.cloud.open.openapi.annotation.OpenApiMethod;
import lombok.Data;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * openapi处理器（具体的处理方法）
 *
 * @author wanghuidong
 */
@Data
public class ApiHandler {

    /**
     * 开放api名称
     */
    private String openApiName;

    /**
     * 开放api方法名称
     */
    private String openApiMethodName;

    /**
     * openapi处理对象名称
     */
    private String beanName;

    /**
     * openapi处理对象
     */
    private Object bean;

    /**
     * openapi处理方法
     */
    private Method method;

    /**
     * 方法参数类型(Class类型信息不完整，无法提取List里元素的类型)
     */
    private Type[] paramTypes;

    /**
     * 方法参数（包括参数名、修饰符等）
     */
    private Parameter[] parameters;

    /**
     * 方法的注解
     */
    private OpenApiMethod openApiMethod;

    @Override
    public String toString() {
        return String.format("%s:%s:%s", bean.getClass().getSimpleName(), method.getName(), Arrays.asList(paramTypes));
    }
}
