package com.taotao.cloud.common.support.reflect.meta.annotation.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.taotao.cloud.common.support.reflect.meta.annotation.IAnnotationTypeMeta;
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.collection.MapUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.reflect.ReflectAnnotationUtil;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象注解类型元数据
 */
public abstract class AbstractAnnotationTypeMeta implements IAnnotationTypeMeta {

    /**
     * 注解引用 map
     */
    private Map<String, Annotation> annotationRefMap;

    /**
     * 获取对应的注解信息列表
     *
     * （1）这里其实没有必要使用 {@link Map} 因为一般注解数量不会太多，只是数组性能反而更好。
     *
     * @return 注解数组
     */
    protected abstract Annotation[] getAnnotations();

    protected AbstractAnnotationTypeMeta() {
        annotationRefMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isAnnotated(String annotationName) {
        Annotation annotation = this.getAnnotation(annotationName);
        return ObjectUtil.isNotNull(annotation);
    }

    @Override
    public Annotation getAnnotation(String annotationName) {
        ArgUtil.notEmpty(annotationName, "annotationName");

        Optional<Annotation> annotationOptional = getAnnotationOpt(getAnnotations(), annotationName);
        return annotationOptional.orElse(null);
    }

    @Override
    public boolean isAnnotatedOrRef(String annotationName) {
        // 直接注解
        if(isAnnotated(annotationName)) {
            return true;
        }

        // 元注解
        List<Annotation> annotationRefs = getAnnotationRefs(annotationName);
        return CollectionUtil.isNotEmpty(annotationRefs);
    }

    @Override
    public boolean isAnnotatedOrRef(List<Class> classList) {
        if(CollectionUtil.isEmpty(classList)) {
            return false;
        }

        for(Class clazz : classList) {
            if(isAnnotatedOrRef(clazz.getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isAnnotationRef(Class clazz) {
        return isAnnotatedOrRef(clazz.getName()) && !isAnnotated(clazz.getName());
    }

    @Override
    public List<Annotation> getAnnotationOrRefs(String annotationName) {
        Set<Annotation> annotationSet = Sets.newHashSet();

        // 直接注解
        Annotation annotation = getAnnotation(annotationName);
        if(ObjectUtil.isNotNull(annotation)) {
            annotationSet.add(annotation);
        }

        // 元注解列表
        List<Annotation> annotationRefList = getAnnotationRefs(annotationName);
        annotationSet.addAll(annotationRefList);

        // 构建结果
        return Lists.newArrayList(annotationSet);
    }

    /**
     * 获取注解对应信息
     * @param annotations 注解数组
     * @param annotationName 指定注解名称
     * @return 结果信息
     */
    private Optional<Annotation> getAnnotationOpt(final Annotation[] annotations, final String annotationName) {
        List<Annotation> annotationList = ArrayUtil.toList(annotations);
        return getAnnotationOpt(annotationList, annotationName);
    }

    /**
     * 获取注解对应信息
     * @param annotations 注解列表
     * @param annotationName 指定注解名称
     * @return 结果信息
     */
    private Optional<Annotation> getAnnotationOpt(final List<Annotation> annotations, final String annotationName) {
        if(CollectionUtil.isEmpty(annotations)) {
            return Optional.empty();
        }

        for(Annotation annotation : annotations) {
            if(annotation.annotationType().getName().equals(annotationName)) {
                return Optional.ofNullable(annotation);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Annotation> getAnnotationRefs(String annotationName) {
        Set<Annotation> annotationSet = Sets.newHashSet();

        if(ArrayUtil.isNotEmpty(getAnnotations())) {
            for(Annotation annotation : getAnnotations()) {
                Annotation[] annotationRefs = annotation.annotationType().getAnnotations();
                Optional<Annotation> annotationRefOptional = getAnnotationOpt(annotationRefs, annotationName);
                if(annotationRefOptional.isPresent()) {
                    // 添加引用属性（注解全称+引用的注解全称）
                    final String key = annotationName+annotation.annotationType().getName();
                    annotationRefMap.put(key, annotationRefOptional.get());

                    annotationSet.add(annotation);
                }
            }
        }

        return Lists.newArrayList(annotationSet);
    }

    @Override
    public Annotation getAnnotationReferenced(String annotationName, String annotationRefName) {
        ArgUtil.notEmpty(annotationName, "annotationName");
        ArgUtil.notEmpty(annotationRefName, "annotationRefName");
        final String key = annotationName+annotationRefName;
        return annotationRefMap.get(key);
    }

    @Override
    public Map<String, Object> getAnnotationAttributes(String annotationName) {
        ArgUtil.notEmpty(annotationName, "annotationName");

        Annotation annotation = this.getAnnotation(annotationName);
        if(ObjectUtil.isNull(annotation)) {
            return null;
        }

        return ReflectAnnotationUtil.getAnnotationAttributes(annotation);
    }

    @Override
    public Map<String, Object> getAnnotationOrRefAttributes(String annotationName) {
        ArgUtil.notEmpty(annotationName, "annotationName");

        List<Annotation> annotationList = this.getAnnotationOrRefs(annotationName);
        if(CollectionUtil.isEmpty(annotationList)) {
            return null;
        }

        // 遍历选择第一个直接返回
        Annotation annotation = annotationList.get(0);
        return ReflectAnnotationUtil.getAnnotationAttributes(annotation);
    }

    @Override
    public Object getAnnotationOrRefAttribute(String annotationName, String attrMethodName) {
        ArgUtil.notEmpty(annotationName, "annotationName");
        ArgUtil.notEmpty(attrMethodName, "attrMethodName");

        Map<String, Object> attrMap = getAnnotationOrRefAttributes(annotationName);
        if(MapUtil.isEmpty(attrMap)) {
            return null;
        }

        return attrMap.get(attrMethodName);
    }

    @Override
    public Object getAnnotationAttr(Annotation annotation, String methodName) {
        ArgUtil.notNull(annotation, "annotation");
        ArgUtil.notEmpty(methodName, "methodName");

        Map<String, Object> attrs = ReflectAnnotationUtil.getAnnotationAttributes(annotation);
        return attrs.get(methodName);
    }

    @Override
    public Object getAnnotatedAttr(Class<? extends Annotation> clazz, String methodName) {
        ArgUtil.notNull(clazz, "clazz");
        ArgUtil.notEmpty(methodName, "methodName");

        Annotation annotation = getAnnotation(clazz.getName());
        if(ObjectUtil.isNotNull(annotation)) {
            return getAnnotationAttr(annotation, methodName);
        }

        return null;
    }

    @Override
    public Object getAnnotationReferencedAttr(Class<? extends Annotation> clazz, String methodName) {
        ArgUtil.notNull(clazz, "clazz");
        ArgUtil.notEmpty(methodName, "methodName");
        final String annotationName = clazz.getName();

        if(ArrayUtil.isNotEmpty(getAnnotations())) {
            for(Annotation annotation : getAnnotations()) {
                Annotation[] annotationRefs = annotation.annotationType().getAnnotations();

                if(ArrayUtil.isNotEmpty(annotationRefs)) {
                    for(Annotation annotationRef : annotationRefs) {
                        if(annotationName.equals(annotationRef.annotationType().getName())) {
                            return getAnnotationAttr(annotationRef, methodName);
                        }
                    }
                }
            }
        }

        return null;
    }

}
