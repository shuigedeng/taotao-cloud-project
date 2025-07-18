package com.taotao.cloud.cache.simple;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

// 注意：继承 StdScalarDeserializer 而非直接实现 StdDeserializer
public class CustomFieldDeserializer<T> extends StdScalarDeserializer<T> {
    // 保存原始反序列化器（关键修复点）
    private final StdScalarDeserializer<T> originalDeserializer;

    // 必须通过构造函数传入原始反序列化器和类型
    public CustomFieldDeserializer(Class<T> type, StdScalarDeserializer<T> originalDeserializer) {
        super(type);
        this.originalDeserializer = originalDeserializer;
        // 确保原始反序列化器被正确关联
        if (this.originalDeserializer == null) {
            throw new IllegalArgumentException("Original deserializer cannot be null");
        }
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 1. 使用原始反序列化器进行基础反序列化
        T value = originalDeserializer.deserialize(p, ctxt);
        
        // 2. 执行自定义业务处理（示例：字符串转大写）
        if (value instanceof String) {
            return (T) ((String) value).toUpperCase();
        }
        
        return value;
    }

    // 修复类型反序列化方法（关键修复点）
    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return originalDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
    }

    // 确保反序列化器可复制（Jackson 2.19+ 要求）
}
