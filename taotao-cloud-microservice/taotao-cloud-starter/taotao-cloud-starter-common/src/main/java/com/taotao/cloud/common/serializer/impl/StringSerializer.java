package com.taotao.cloud.common.serializer.impl;

import com.taotao.cloud.common.serializer.Serializer;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

/**
 * 字符串序列化
 */
public class StringSerializer implements Serializer {
    @Override
    public String name() {
        return "string";
    }

    @Override
    public byte[] serialize(Object data){
        if(data == null) {
            return new byte[0];
        }
        return ((String)data).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Object deserialize(byte[] bytes,ClassLoader classLoader) {
        if(bytes == null) {
            return null;
        }
        return new String(bytes,StandardCharsets.UTF_8);
    }
}
