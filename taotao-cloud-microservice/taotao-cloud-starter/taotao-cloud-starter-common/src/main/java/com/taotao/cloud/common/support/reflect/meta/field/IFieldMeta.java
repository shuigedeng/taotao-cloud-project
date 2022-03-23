package com.taotao.cloud.common.support.reflect.meta.field;

import java.lang.reflect.Field;

/**
 * 字段原始数据信息
 */
public interface IFieldMeta {

    /**
     * 设置名称
     * @param name 名称
     */
    void setName(final String name);

    /**
     * 字段名称
     * @return 名称
     */
    String getName();

    /**
     * 设置字段类型
     * @param type 类型
     */
    void setType(final Class type);

    /**
     * 字段类型
     * @return 字段类型
     */
    Class getType();

    /**
     * 字段值
     * @return 字段值
     */
    Object getValue();

    /**
     * 字段值
     * @param value 设置值
     */
    void setValue(final Object value);

    /**
     * 基本元素类型
     * @param type 类型
     */
    void setComponentType(final Class type);

    /**
     * 获取元素类型
     * @return 类型
     */
    Class getComponentType();

    /**
     * 设置 field 信息
     * @param field field
     */
    void setField(final Field field);


    /**
     * 获取 field 信息
     * @return field 信息
     */
    Field getField();

}
