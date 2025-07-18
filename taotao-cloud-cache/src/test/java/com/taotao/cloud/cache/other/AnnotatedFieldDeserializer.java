package com.taotao.cloud.cache.other;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class AnnotatedFieldDeserializer extends StdDeserializer<Object> {

    private final JsonDeserializer<?> defaultDeserializer;
    
    public AnnotatedFieldDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(defaultDeserializer.handledType());
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 先使用默认反序列化器获取值
        Object value = defaultDeserializer.deserialize(p, ctxt);
        
        // 检查当前字段是否有我们的注解
        if (p.getCurrentToken().isScalarValue()) {  // 只处理基本类型
            String fieldName = p.getCurrentName();
            if (fieldName != null) {
                Object bean = p.getParsingContext().getParent().getCurrentValue();
                try {
                    java.lang.reflect.Field field = bean.getClass().getDeclaredField(fieldName);
                    if (field.isAnnotationPresent(CustomProcess.class)) {
                        // 获取注解配置
                        CustomProcess annotation = field.getAnnotation(CustomProcess.class);
                        // 执行业务处理逻辑
                        return processValue(value, annotation.processor());
                    }
                } catch (NoSuchFieldException e) {
                    // 字段不存在，忽略
                }
            }
        }
        
        return value;
    }

    private Object processValue(Object value, String processor) {
        // 这里实现你的业务逻辑
        if (value instanceof String) {
            return ((String) value).toUpperCase(); // 示例：转为大写
        }
        return value;
    }
}
