package com.taotao.cloud.rpc.serialization;

import com.taotao.cloud.rpc.common.ProtocolConstants;
import com.taotao.cloud.rpc.common.spi.SPI;

/**
 * 序列化器接口
 * <p>
 * 通过 {@link #getContentTypeId()} 区分实现，对应 ttc-RPC 协议的序列化字段。
 * <p>
 * 资源文件：META-INF/ttc-rpc/com.taotao.cloud.rpc.serialize.Serialization
 */
@SPI("hessian2")
public interface Serialization {

    /**
     * 获取序列化器 ID
     * <p>
     * 必须是 {@link ProtocolConstants} 中定义的值。
     */
    byte getContentTypeId();

    /**
     * 序列化对象
     *
     * @param obj 任意对象
     * @return 字节流
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes 字节流
     * @param clazz 目标类型
     * @return 对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

    /**
     * 反序列化（带泛型类型）
     * <p>
     * 默认委托给 {@link #deserialize(byte[], Class)}
     */
    default Object deserialize(byte[] bytes, String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return deserialize(bytes, clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class not found: " + className, e);
        }
    }
}
