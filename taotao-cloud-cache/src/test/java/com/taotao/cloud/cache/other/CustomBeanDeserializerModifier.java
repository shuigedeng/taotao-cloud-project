package com.taotao.cloud.cache.other;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import java.io.IOException;

public class CustomBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config,
                                               BeanDescription beanDesc,
                                               JsonDeserializer<?> deserializer) {
        // 只处理Bean类型的反序列化器
        if (deserializer instanceof BeanDeserializer) {
            return new CustomBeanDeserializer((BeanDeserializer) deserializer, config);
        }
        return deserializer;
    }

    static class CustomBeanDeserializer extends BeanDeserializer {
        private final DeserializationConfig config;

        public CustomBeanDeserializer(BeanDeserializer src, DeserializationConfig config) {
            super(src);
            this.config = config;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            // 先让父类完成基础反序列化
            Object bean = super.deserialize(p, ctxt);
            
            // 后处理：修改带有注解的字段
            if (bean != null) {
                processAnnotatedFields(bean, ctxt);
            }
            return bean;
        }

        private void processAnnotatedFields(Object bean, DeserializationContext ctxt) throws IOException {
            Class<?> beanClass = bean.getClass();
            for (BeanPropertyDefinition propDef : config.introspect(ctxt.getTypeFactory()
                    .constructType(beanClass)).findProperties()) {
                
                if (propDef.hasField() && 
                    propDef.getField().hasAnnotation(CustomProcess.class)) {
                    
                    processField(bean, propDef, ctxt);
                }
            }
        }

        private void processField(Object bean, 
                                BeanPropertyDefinition propDef,
                                DeserializationContext ctxt) throws IOException {
            try {
                AnnotatedField field = propDef.getField();
                field.fixAccess(true); // 确保字段可访问
                
                Object value = field.getValue(bean);
                if (value != null) {
                    CustomProcess annotation = field.getAnnotation(CustomProcess.class);
                    Object processedValue = processValue(value, annotation.processor());
                    field.setValue(bean, processedValue);
                }
            } catch (Exception e) {
                ctxt.handleInstantiationProblem(handledType(), bean, e);
            }
        }

        private Object processValue(Object value, String processor) {
            // 实现业务逻辑
            if (value instanceof String) {
                return ((String) value).toUpperCase(); // 示例处理
            }
            return value;
        }
    }
}
