package com.taotao.cloud.common.support.reflect.meta.annotation.impl;


import com.taotao.cloud.common.utils.common.ArgUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 字段注解类型信息
 * <p> project: heaven-AnnotationMeta </p>
 * <p> create on 2019/11/29 23:04 </p>
 *
 * @author Administrator
 * @since 0.1.55
 */
public class FieldAnnotationTypeMeta extends AbstractAnnotationTypeMeta {

    /**
     * 注解信息
     * @since 0.1.55
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
