package com.taotao.cloud.common.support.reflect.handler;


import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.support.reflect.simple.SimpleAnnotation;
import java.lang.annotation.Annotation;

/**
 * 默认的注解实现类
 */
public class SimpleAnnotationHandler implements IHandler<Annotation, SimpleAnnotation> {

    @Override
    public SimpleAnnotation handle(Annotation annotation) {
        SimpleAnnotation simpleAnnotation = new SimpleAnnotation();
        simpleAnnotation.type(annotation.annotationType());
        return simpleAnnotation;
    }

}
