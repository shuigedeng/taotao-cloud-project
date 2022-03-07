package com.taotao.cloud.core.heaven.reflect.handler;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.reflect.simple.SimpleAnnotation;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import java.lang.annotation.Annotation;

/**
 * 默认的注解实现类
 */
@ThreadSafe
public class SimpleAnnotationHandler implements IHandler<Annotation, SimpleAnnotation> {

    @Override
    public SimpleAnnotation handle(Annotation annotation) {
        SimpleAnnotation simpleAnnotation = new SimpleAnnotation();
        simpleAnnotation.type(annotation.annotationType());
        return simpleAnnotation;
    }

}
