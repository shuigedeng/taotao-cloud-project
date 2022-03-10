package com.taotao.cloud.common.support.reflect.meta.field.impl;


import com.taotao.cloud.common.support.reflect.meta.field.IFieldMeta;
import java.lang.reflect.Field;

/**
 * 字段元数据
 *
 * <p> project: FieldMeta </p>
 * <p> create on 2019/11/9 23:08 </p>
 *
 * @author Administrator
 * @since 0.1.41
 */
public class FieldMeta implements IFieldMeta {

    /**
     * 字段名称
     * @since 0.1.41
     */
    private String name;

    /**
     * 字段类型
     * @since 0.1.41
     */
    private Class type;

    /**
     * 字段值
     * @since 0.1.41
     */
    private Object value;

    /**
     * 元素类型
     * @since 0.1.41
     */
    private Class componentType;

    /**
     * 原始字段信息列表
     * @since 0.1.41
     */
    private Field field;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public void setType(Class type) {
        this.type = type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Class getComponentType() {
        return componentType;
    }

    @Override
    public void setComponentType(Class componentType) {
        this.componentType = componentType;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public void setField(Field field) {
        this.field = field;
    }
}
