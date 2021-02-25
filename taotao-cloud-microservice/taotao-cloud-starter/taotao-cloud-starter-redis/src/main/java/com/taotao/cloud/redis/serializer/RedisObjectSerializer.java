/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.redis.serializer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 此时定义的序列化操作表示可以序列化所有类的对象，当然，这个对象所在的类一定要实现序列化接口
 *
 * @author dengtao
 * @since 2020/4/30 10:14
 * @version 1.0.0
 */
public class RedisObjectSerializer implements RedisSerializer<Object> {

    /**
     * 做一个空数组，不是null
     */
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * 为了方便进行对象与字节数组的转换，所以应该首先准备出两个转换器述
     */
    private final Converter<Object, byte[]> serializingConverter = new SerializingConverter();
    private final Converter<byte[], Object> deSerializingConverter = new DeserializingConverter();

    @Override
    public byte[] serialize(Object obj) {
        // 这个时候没有要序列化的对象出现，所以返回的字节数组应该就是一个空数组
        if (obj == null) {
            return EMPTY_BYTE_ARRAY;
        }
        // 将对象变为字节数组
        return this.serializingConverter.convert(obj);
    }

    @Override
    public Object deserialize(byte[] data) {
        // 此时没有对象的内容信息
        if (data == null || data.length == 0) {
            return null;
        }
        return this.deSerializingConverter.convert(data);
    }

}
