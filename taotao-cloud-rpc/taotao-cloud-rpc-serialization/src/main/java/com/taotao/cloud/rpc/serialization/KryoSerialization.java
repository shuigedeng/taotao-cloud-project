package com.taotao.cloud.rpc.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.taotao.cloud.rpc.serialization.Serialization;
import com.taotao.cloud.rpc.common.RpcException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Kryo 序列化
 * <p>
 * 高性能 Java 序列化，ThreadLocal 缓存 Kryo 实例。
 * <p>
 * 注意：Kryo 对一些类（如 ArrayList 子类）有侵入性，需要注册或使用兼容模式。
 */
public class KryoSerialization implements Serialization {

    private static final Logger log = LogManager.getLogger(KryoSerialization.class);

    /**
     * ThreadLocal 缓存 Kryo 实例（Kryo 非线程安全）
     */
    private static final ThreadLocal<Kryo> KRYO_LOCAL = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setReferences(true);
            kryo.setRegistrationRequired(false);
            return kryo;
        }
    };

    @Override
    public byte getContentTypeId() {
        return com.taotao.cloud.rpc.common.ProtocolConstants.SERIALIZE_KRYO;
    }

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) return new byte[0];
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Output out = new Output(baos)) {
            Kryo kryo = KRYO_LOCAL.get();
            kryo.writeClassAndObject(out, obj);
            out.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Kryo serialize failed: {}", e.getMessage(), e);
            throw RpcException.serialization("Kryo serialize failed: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) return null;
        try (Input in = new Input(new ByteArrayInputStream(bytes))) {
            Kryo kryo = KRYO_LOCAL.get();
            return (T) kryo.readClassAndObject(in);
        } catch (Exception e) {
            log.error("Kryo deserialize failed: {}", e.getMessage(), e);
            throw RpcException.serialization("Kryo deserialize failed: " + e.getMessage(), e);
        }
    }
}
