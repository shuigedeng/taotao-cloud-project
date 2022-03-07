package com.taotao.cloud.core.heaven.reflect.meta.annotation.impl;


import com.taotao.cloud.core.heaven.util.common.ArgUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 方法级别注解信息
 *
 * <p> project: heaven-AnnotationMeta </p>
 * <p> create on 2019/11/29 23:04 </p>
 *
 * @author Administrator
 * @since 0.1.52
 */
public class MethodAnnotationTypeMeta extends AbstractAnnotationTypeMeta {

    /**
     * 注解信息
     * （1）这里其实没有必要使用 {@link Map} 因为一般注解数量不会太多，只是数组性能反而更好。
     * @since 0.1.52
     */
    private Annotation[] annotations;

    public MethodAnnotationTypeMeta(final Method method) {
        ArgUtil.notNull(method, "method");

        annotations = method.getAnnotations();
    }

    @Override
    protected Annotation[] getAnnotations() {
        return annotations;
    }

}
