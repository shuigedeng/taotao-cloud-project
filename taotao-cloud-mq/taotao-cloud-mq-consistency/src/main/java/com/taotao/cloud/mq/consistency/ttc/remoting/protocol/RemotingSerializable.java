package com.taotao.cloud.mq.consistency.ttc.remoting.protocol;


import com.taotao.boot.common.utils.json.JacksonUtils;
import tools.jackson.core.type.TypeReference;

import java.nio.charset.StandardCharsets;

/**
 * 远程通信序列化接口
 * 提供对象与字节数组/JSON字符串之间的转换
 */
public abstract class RemotingSerializable {

    /**
     * 从字节数组解码对象
     */
    public static <T> T decode(byte[] data, Class<T> clazz) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            return JacksonUtils.toObject(new String(data, StandardCharsets.UTF_8), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从字节数组解码对象（带泛型）
     */
    public static <T> T decode(byte[] data, TypeReference<T> typeReference) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            String json = new String(data, StandardCharsets.UTF_8);
            return JacksonUtils.toObject(json, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从字符串解码对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JacksonUtils.toObject(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从字符串解码对象（带泛型）
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JacksonUtils.toObject(json, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将对象转换为字节数组（静态方法）
     */
    public static byte[] encode(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return JacksonUtils.toJson(obj).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将对象转换为JSON字符串（静态方法）
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return JacksonUtils.toJson(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将对象编码为字节数组（JSON格式）
     */
    public byte[] encode() {
        try {
            return JacksonUtils.toJson(this).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将对象编码为JSON字符串
     */
    public String toJson() {
        try {
            return JacksonUtils.toJson(this);
        } catch (Exception e) {
            return null;
        }
    }
}
