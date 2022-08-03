package com.taotao.cloud.sys.biz.api.controller.tools.zookeeper.service;

import com.taotao.cloud.sys.biz.api.controller.tools.serializer.service.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.IOException;

@Slf4j
public class ZkSerializerAdapter implements ZkSerializer {
    private Serializer serializer;

    public ZkSerializerAdapter(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public byte[] serialize(Object o) throws ZkMarshallingError {
        try {
            return serializer.serialize(o);
        } catch (IOException e) {
            log.error("serialize error : {}",e.getMessage(),e);
        }
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            return serializer.deserialize(bytes,ClassLoader.getSystemClassLoader());
        } catch (IOException | ClassNotFoundException e) {
            log.error("deserialize error : {}",e.getMessage(),e);
        }
        return null;
    }
}
