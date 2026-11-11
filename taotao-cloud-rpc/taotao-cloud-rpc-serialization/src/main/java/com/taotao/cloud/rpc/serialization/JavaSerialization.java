package com.taotao.cloud.rpc.serialization;

import com.taotao.cloud.rpc.serialization.Serialization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Java 原生序列化
 */
public class JavaSerialization implements Serialization {

    private static final Logger log = LogManager.getLogger(JavaSerialization.class);

    @Override
    public byte getContentTypeId() {
        return com.taotao.cloud.rpc.common.ProtocolConstants.SERIALIZE_JAVA;
    }

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) return new byte[0];
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Java serialize failed: {}", e.getMessage(), e);
            throw com.taotao.cloud.rpc.common.RpcException.serialization(
                    "Java serialize failed: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            log.error("Java deserialize failed: {}", e.getMessage(), e);
            throw com.taotao.cloud.rpc.common.RpcException.serialization(
                    "Java deserialize failed: " + e.getMessage(), e);
        }
    }
}
