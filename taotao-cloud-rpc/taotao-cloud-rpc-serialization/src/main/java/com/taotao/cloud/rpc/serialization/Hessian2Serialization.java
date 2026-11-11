package com.taotao.cloud.rpc.serialization;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.taotao.cloud.rpc.serialization.Serialization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian2 序列化
 * <p>
 * Dubbo 默认序列化方案，性能好、跨语言、支持复杂对象。
 */
public class Hessian2Serialization implements Serialization {

    private static final Logger log = LogManager.getLogger(Hessian2Serialization.class);

    @Override
    public byte getContentTypeId() {
        return com.taotao.cloud.rpc.common.ProtocolConstants.SERIALIZE_HESSIAN2;
    }

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) return new byte[0];
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Hessian2Output out = new Hessian2Output(baos);
            try {
                out.writeObject(obj);
                out.flush();
            } finally {
                out.close();
            }
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Hessian2 serialize failed: {}", e.getMessage(), e);
            throw com.taotao.cloud.rpc.common.RpcException.serialization(
                    "Hessian2 serialize failed: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            Hessian2Input in = new Hessian2Input(bais);
            try {
                return (T) in.readObject(clazz);
            } finally {
                in.close();
            }
        } catch (Exception e) {
            log.error("Hessian2 deserialize failed: {}", e.getMessage(), e);
            throw com.taotao.cloud.rpc.common.RpcException.serialization(
                    "Hessian2 deserialize failed: " + e.getMessage(), e);
        }
    }
}
