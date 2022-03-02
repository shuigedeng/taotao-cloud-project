package com.taotao.cloud.common.serializer.impl;

import com.taotao.cloud.common.serializer.Serializer;
import com.taotao.cloud.common.serializer.SerializerConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.springframework.stereotype.Component;

public class JdkSerializer implements Serializer {
    @Override
    public String name() {
        return SerializerConstants.JDK;
    }

    @Override
    public byte[] serialize(Object o) throws IOException {
        if(o == null) {
            return new byte[0];
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(o);

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes,ClassLoader classLoader) throws IOException, ClassNotFoundException {
        if(bytes == null) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        CustomObjectInputStream objectInputStream = new CustomObjectInputStream(byteArrayInputStream,classLoader);

        return  objectInputStream.readObject();
    }
}
