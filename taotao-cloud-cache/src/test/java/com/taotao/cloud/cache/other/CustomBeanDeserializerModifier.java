package com.taotao.cloud.cache.other;

import com.google.gson.JsonDeserializer;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.*;
import tools.jackson.databind.deser.*;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.introspect.AnnotatedField;
import tools.jackson.databind.introspect.BeanPropertyDefinition;
import java.io.IOException;

public class CustomBeanDeserializerModifier extends ValueDeserializerModifier {

	@Override
	public ValueDeserializer<?> modifyDeserializer( DeserializationConfig config,
		BeanDescription.Supplier beanDescRef,
		ValueDeserializer<?> deserializer ) {

// 只处理Bean类型的反序列化器
		if (deserializer instanceof ValueDeserializer) {
			return new CustomBeanDeserializer((ValueDeserializer) deserializer, config);
		}
		return deserializer;
	}


    static class CustomBeanDeserializer extends ValueDeserializer {
        private final DeserializationConfig config;

        public CustomBeanDeserializer(ValueDeserializer src, DeserializationConfig config) {
            this.config = config;
        }

		@Override
		public Object deserialize( JsonParser p, DeserializationContext ctxt ) throws JacksonException {
			return null;
		}

		@Override
		public Object deserialize( JsonParser p, DeserializationContext ctxt, Object obj ) throws JacksonException {
			// 先让父类完成基础反序列化
			Object bean = super.deserialize(p, ctxt,obj);
// 后处理：修改带有注解的字段
			if (bean != null) {
				try {
					processAnnotatedFields(bean, ctxt);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return bean;
		}

        private void processAnnotatedFields(Object bean, DeserializationContext ctxt) throws IOException {
            Class<?> beanClass = bean.getClass();

            for (BeanPropertyDefinition propDef : config.classIntrospectorInstance().introspectForDeserialization(ctxt.getTypeFactory()
				.constructType(beanClass), null).findProperties()) {
                
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
