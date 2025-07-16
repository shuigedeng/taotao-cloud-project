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

package com.taotao.cloud.rpc.registry.apiregistry.code;

import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ProtostuffCode implements ICode {
    public <T> byte[] encode(T data) {
        // return ProtostuffUtils.serialize(data);
        return null;
    }

    public <T> T decode(byte[] data, Type type) {
        if (type instanceof ParameterizedType) {
            // return
            // ProtostuffUtils.deserialize(data,(Class<T>)((ParameterizedType)type).getRawType());
            return null;
        }
        if (type instanceof Class<?>) {
            // return ProtostuffUtils.deserialize(data,(Class<T>)type);
            return null;
        }
        throw new ApiRegistryException("Protostuff不支持该类型反序列化");
    }
}
