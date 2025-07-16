/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.rpc.common.common;

/**
 * 序列化utils
 *
 * @author shuigedeng
 * @version v1.0.0
 */
public class SerializationUtil {
    //    private static Map<Class<?>, Schema<?>> cachedMap = new ConcurrentHashMap<>();
    //    private static Objenesis objenesis = new ObjenesisStd(true);
    //
    //    /**
    //     * 获取类型的 Schema
    //     *
    //     * @param clazz 类型
    //     * @return com.dyuproject.protostuff.Schema<T>
    //     * @author shuigedeng
    //     * @since 2024.06
    //     */
    //    public static <T> Schema<T> getSchema(Class<T> clazz) {
    //        Schema<T> schema = (Schema<T>) cachedMap.get(clazz);
    //        if (Objects.isNull(schema)) {
    //            schema = RuntimeSchema.createFrom(clazz);
    //            cachedMap.put(clazz, schema);
    //        }
    //        return schema;
    //    }
    //
    //    /**
    //     * 序列号对象
    //     *
    //     * @param obj 类型
    //     * @return byte[]
    //     * @author shuigedeng
    //     * @since 2024.06
    //     */
    //    public static <T> byte[] serialize(T obj) throws IllegalStateException {
    //        Class<T> clazz = (Class<T>) obj.getClass();
    //        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    //        try {
    //            Schema<T> schema = getSchema(clazz);
    //            return ProtobufIOUtil.toByteArray(obj, schema, buffer);
    //        } catch (Exception e) {
    //            throw new IllegalStateException(e.getMessage(), e);
    //        } finally {
    //            buffer.clear();
    //        }
    //    }
    //
    //    /**
    //     * 反序列化对象
    //     *
    //     * @param data  数据
    //     * @param clazz 类型
    //     * @return T 类
    //     * @author shuigedeng
    //     * @since 2020/2/27 11:03
    //     */
    //    public static <T> T deserialize(byte[] data, Class<T> clazz) throws IllegalStateException
    // {
    //        try {
    //            T message = objenesis.newInstance(clazz);
    //            Schema<T> schema = getSchema(clazz);
    //            ProtobufIOUtil.mergeFrom(data, message, schema);
    //            return message;
    //        } catch (Exception e) {
    //            throw new IllegalStateException(e.getMessage(), e);
    //        }
    //    }
}
