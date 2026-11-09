package com.taotao.cloud.mq.consistency.ttc.common.util;


import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@code AtomicLong / AtomicInteger / AtomicBoolean} 字段的标量编解码器。
 * <p>
 * 挂给 z-util JsonUtil 的 {@code @JsonSerialize} / {@code @JsonDeserialize}：
 * 这三种类型的字段是 {@code final} 的（只能 set 一次），JsonUtil 的默认 POJO 分支
 * 会去 new 它们并把 Integer 直接 set 进去，抛
 * {@code Can not set final AtomicLong field ... to Integer}（实测）。
 * 显式给出标量编解码后，线上 JSON 形状与 Jackson 一致（就是一个裸数字 / 裸布尔）。
 */
public final class AtomicNumberSerializer implements ValueSerializer, ValueDeserializer {

    @Override
    public String serialize(Object value, String format) {
        if (value == null) {
            return "null";
        }
        if (value instanceof AtomicLong) {
            return String.valueOf(((AtomicLong) value).get());
        }
        if (value instanceof AtomicInteger) {
            return String.valueOf(((AtomicInteger) value).get());
        }
        return String.valueOf(((AtomicBoolean) value).get());
    }

    @Override
    public Object deserialize(String value, Class<?> targetType, String format) {
        if (value == null || "null".equals(value)) {
            return null;
        }
        if (targetType == AtomicBoolean.class) {
            return new AtomicBoolean(Boolean.parseBoolean(value));
        }
        if (targetType == AtomicInteger.class) {
            return new AtomicInteger(Integer.parseInt(value.trim()));
        }
        return new AtomicLong(Long.parseLong(value.trim()));
    }

	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
		return null;
	}

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {

	}
}
