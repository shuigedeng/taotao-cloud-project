package com.taotao.cloud.cache.simple;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import java.util.List;

public class CustomBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, 
                                                 BeanDescription beanDesc, 
                                                 JsonDeserializer<?> deserializer) {
        
        // 获取类中所有字段定义
        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        
        for (BeanPropertyDefinition prop : properties) {
            // 检查字段是否带有自定义注解
            if (prop.getField().getAnnotation(SpecialField.class) != null) {
                // 只处理标量类型（String、Number等），确保原始反序列化器类型正确
                if (deserializer instanceof StdScalarDeserializer) {
                    // 强制转换为标量反序列化器（Jackson 2.19+ 类型检查更严格）
                    StdScalarDeserializer<?> scalarDeserializer = (StdScalarDeserializer<?>) deserializer;
                    // 创建包装器，传递原始反序列化器（关键修复点）
//                    return new CustomFieldDeserializer<>(prop.getRawPrimaryType(), scalarDeserializer);
                }
            }
        }
        
        // 非注解字段使用默认反序列化器
        return super.modifyDeserializer(config, beanDesc, deserializer);
    }
}
