package com.taotao.cloud.ccsr.core.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.taotao.cloud.ccsr.spi.Join;
import com.taotao.cloud.ccsr.common.utils.ByteUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * hessian serializer.
 */
@Join
@SuppressWarnings("all")
public class HessianSerializer implements Serializer {

    private SerializerFactory serializerFactory = new SerializerFactory();

    public HessianSerializer() {
    }

    @Override
    public <T> T deserialize(byte[] data) {
        return deseiralize0(data);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        return deserialize(data);
    }

    @Override
    public <T> T deserialize(byte[] data, Type type) {
        return deserialize(data);
    }

    private <T> T deseiralize0(byte[] data) {
        if (ByteUtils.isEmpty(data)) {
            return null;
        }

        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(data));
        input.setSerializerFactory(serializerFactory);
        Object resultObject;
        try {
            resultObject = input.readObject();
            input.close();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred when Hessian serializer decode!", e);
        }
        return (T) resultObject;
    }

    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(byteArray);
        output.setSerializerFactory(serializerFactory);
        try {
            output.writeObject(obj);
            output.close();
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred when Hessian serializer encode!", e);
        }

        return byteArray.toByteArray();
    }

    @Override
    public String name() {
        return "Hessian";
    }

}
