package com.taotao.cloud.sys.biz.modules.serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.taotao.cloud.sys.biz.modules.serializer.SerializerConstants;
import com.taotao.cloud.sys.biz.modules.serializer.service.Serializer;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * kryo 序列化
 */
@Component
public class KryoSerializer implements Serializer {
    public static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(
                    new StdInstantiatorStrategy()));
            return kryo;
        };
    };


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
