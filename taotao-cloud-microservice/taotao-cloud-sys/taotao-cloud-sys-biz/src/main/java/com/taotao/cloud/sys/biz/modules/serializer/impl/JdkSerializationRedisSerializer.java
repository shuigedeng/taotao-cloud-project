package com.taotao.cloud.sys.biz.modules.serializer.impl;

import com.taotao.cloud.sys.biz.modules.serializer.SerializerConstants;
import com.taotao.cloud.sys.biz.modules.serializer.service.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ConfigurableObjectInputStream;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 这个序列化类对应 spring 的 org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
 */
@Component
public class JdkSerializationRedisSerializer implements Serializer {
    @Autowired
    private JdkSerializer jdkSerializer;

    @Override
    public String name() {
        return "dubboJdk";
    }

    @Override
    public byte[] serialize(Object data) throws IOException {
        final byte[] serialize = jdkSerializer.serialize(data);
        return jdkSerializer.serialize(serialize);
    }

    @Override
    public Object deserialize(byte[] bytes, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(byteStream, classLoader);
        final Object object = objectInputStream.readObject();
        return jdkSerializer.deserialize((byte[]) object,classLoader);
    }
}
