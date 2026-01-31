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

package com.taotao.cloud.netty.itcast.protocol;

import com.google.gson.*;
import jakarta.json.Json;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 用于扩展序列化、反序列化算法
 */
public interface Serializer {

    // 反序列化方法
    <T> T deserialize( Class<T> clazz, byte[] bytes );

    // 序列化方法
    <T> byte[] serialize( T object );

    enum Algorithm implements Serializer {
        Java {
            @Override
            public <T > T deserialize(Class < T > clazz, byte[] bytes){
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("反序列化失败", e);
                }
            }

            @Override
            public <T > byte[] serialize ( T object){
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("序列化失败", e);
                }
            }
        },

        Json{
            @Override
            public <T > T deserialize(Class < T > clazz, byte[] bytes){
                Gson gson =
                        new GsonBuilder()
                                .registerTypeAdapter(Class.class, new ClassCodec())
                                .create();
                String json = new String(bytes, StandardCharsets.UTF_8);
                return gson.fromJson(json, clazz);
            }

            @Override
            public <T > byte[] serialize ( T object){
                Gson gson =
                        new GsonBuilder()
                                .registerTypeAdapter(Class.class, new ClassCodec())
                                .create();
                String json = gson.toJson(object);
                return json.getBytes(StandardCharsets.UTF_8);
            }
        }

    }

    /**
     * ClassCodec
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public Class<?> deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context )
                throws JsonParseException {
            try {
                String str = json.getAsString();
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override //   String.class
        public JsonElement serialize(
                Class<?> src, Type typeOfSrc, JsonSerializationContext context ) {
            // class -> json
            return new JsonPrimitive(src.getName());
        }
    }
}
