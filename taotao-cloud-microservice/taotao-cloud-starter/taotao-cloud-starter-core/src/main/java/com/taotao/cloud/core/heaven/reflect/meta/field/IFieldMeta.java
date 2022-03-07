package com.taotao.cloud.core.heaven.reflect.meta.field;

import java.lang.reflect.Field;

/**
 * 字段原始数据信息
 * （1）可以全部归集到 Heaven 项目
 * （2）可以提供相关的工具类。
 *
 * <p> project: IFieldMeta </p>
 * <p> create on 2019/11/9 23:08 </p>
 *
 * @author Administrator
 * @since 0.1.41
 */
public interface IFieldMeta {

    /**
     * 设置名称
     * @param name 名称
     * @since 0.1.41
     */
    void setName(final String name);

    /**
     * 字段名称
     * @return 名称
     * @since 0.1.41
     */
    String getName();

    /**
     * 设置字段类型
     * @param type 类型
     * @since 0.1.41
     */
    void setType(final Class type);

    /**
     * 字段类型
     * @return 字段类型
     * @since 0.1.41
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
     * @since 0.1.41
     */
    void setComponentType(final Class type);

    /**
     * 获取元素类型
     * @return 类型
     * @since 0.1.41
     */
    Class getComponentType();

    /**
     * 设置 field 信息
     * @param field field
     * @since 0.1.41
     */
    void setField(final Field field);


    /**
     * 获取 field 信息
     * @since 0.1.41
     * @return field 信息
     */
    Field getField();

}
