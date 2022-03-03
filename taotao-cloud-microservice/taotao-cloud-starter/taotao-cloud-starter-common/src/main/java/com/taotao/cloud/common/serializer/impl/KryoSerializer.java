package com.taotao.cloud.common.serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.taotao.cloud.common.serializer.Serializer;
import com.taotao.cloud.common.serializer.SerializerConstants;
import java.io.ByteArrayOutputStream;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.stereotype.Component;

/**
 * kryo 序列化
 */
public class KryoSerializer implements Serializer {
    public static final ThreadLocal<Kryo> kryos = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(
                new StdInstantiatorStrategy()));
        return kryo;
    });


    @Override
    public String name() {
        return SerializerConstants.KRYO;
    }

    @Override
    public byte[] serialize(Object data)  {
        if (data == null) {
            return new byte[0];
        }
        Kryo kryo = kryos.get();
        kryo.setReferences(false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, data);
        output.flush();
        return baos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes,ClassLoader classLoader)  {
        if (bytes == null) {
            return null;
        }

        Kryo kryo = kryos.get();
        kryo.setClassLoader(classLoader);
        kryo.setReferences(false);
        Input input = new Input(bytes);
        return kryo.readClassAndObject(input);
    }
}
