package com.taotao.cloud.mq.consistency.ttc.common.util;


import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * byte[] 字段的 Base64 编解码器。
 * <p>
 * 挂给 z-util JsonUtil 的 {@code @JsonSerialize} / {@code @JsonDeserialize}：
 * Jackson 的 {@code writeValueAsBytes}/{@code readValue(byte[])} 一直是「base64 文本」这语义，
 * 而 z-util 的默认数组分支会把 byte[] 写成 {@code [1,2,3]} 数字数组、读回来时
 * {@code Array.set} 直接抛 {@code argument type mismatch}（实测）。协议层的老对端
 * 也都按 base64 读，所以这里显式把 byte[] 拉回 base64，帧格式保持逐字节兼容。
 */
public final class Base64Serializer implements ValueSerializer, ValueDeserializer {

    @Override
    public String serialize(Object value, String format) {
        if (value == null) {
            return "null";
        }
        return "\"" + Base64.getEncoder().encodeToString((byte[]) value) + "\"";
    }

    @Override
    public Object deserialize(String value, Class<?> targetType, String format) {
        if (value == null || "null".equals(value)) {
            return null;
        }
        return Base64.getDecoder().decode(value.getBytes(StandardCharsets.US_ASCII));
    }
}
