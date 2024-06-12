package com.taotao.cloud.rpc.common.common.rpc.codec;

/**
 * 序列化相关处理
 * @author shuigedeng
 * @since 2024.06
 */
public interface RpcCodec {

    /**
     * 对象转数组
     * @param object 对象
     * @return 数组
     * @since 2024.06
     */
    byte[] toBytes(final Object object);

    /**
     * 字节数组转对象
     * @param bytes 字节信息
     * @param tClass 类
     * @param <T> 泛型
     * @return 对象信息
     * @since 2024.06
     */
    <T> T toObject(final byte[] bytes, final Class<T> tClass);

}
