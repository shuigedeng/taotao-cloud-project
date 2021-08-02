package org.apache.dubbo.config.spring.beans.factory.annotation;

import static com.alibaba.spring.util.AnnotationUtils.getAttributes;
import static org.springframework.core.annotation.AnnotationAttributes.fromMap;

import feign.Feign;
import feign.Target;
import java.util.Objects;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

/**
 * Dubbo、Feign整合类
 *
 * @author shuigedeng
 */
public class DubboFeignBuilder extends Feign.Builder {

    @Autowired
    @SuppressWarnings("all")
    private ApplicationContext applicationContext;

    public DubboReference defaultReference;

    static final class DefaultReferenceClass {
        @DubboReference(check = false)
        String field;
    }

    public DubboFeignBuilder() {
        this.defaultReference = Objects.requireNonNull(ReflectionUtils.findField(DefaultReferenceClass.class, "field")).getAnnotation(DubboReference.class);
    }


    @Override
    public <T> T target(Target<T> target) {
        ReferenceBeanBuilder beanBuilder = ReferenceBeanBuilder.create(fromMap(getAttributes(defaultReference,
                applicationContext.getEnvironment(), true)), applicationContext).interfaceClass(target.type());
        try {
            T object = (T) beanBuilder.build().getObject();
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
