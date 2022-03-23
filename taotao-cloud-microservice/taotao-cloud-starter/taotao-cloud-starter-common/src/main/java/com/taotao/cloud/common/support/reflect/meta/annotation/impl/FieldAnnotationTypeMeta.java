package com.taotao.cloud.common.support.reflect.meta.annotation.impl;


import com.taotao.cloud.common.utils.common.ArgUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 字段注解类型信息
 */
public class FieldAnnotationTypeMeta extends AbstractAnnotationTypeMeta {

    /**
     * 注解信息
     */
    private Annotation[] annotations;

    public FieldAnnotationTypeMeta(Field field) {
        ArgUtil.notNull(field, "field");

        annotations = field.getAnnotations();
    }

    @Override
    protected Annotation[] getAnnotations() {
        return annotations;
    }

}
