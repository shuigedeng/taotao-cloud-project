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

package com.taotao.cloud.rpc.common.common.rpc.codec.impl;

import com.taotao.cloud.rpc.common.common.rpc.codec.RpcCodec;

/**
 * 默认序列化相关处理
 * TODO: 对于 object 的处理会导致死循环。后期修复掉这个 bug。
 * @author shuigedeng
 * @since 2024.06
 */
public class SimpleRpcCodec implements RpcCodec {
    @Override
    public byte[] toBytes(Object object) {
        return new byte[0];
    }

    @Override
    public <T> T toObject(byte[] bytes, Class<T> tClass) {
        return null;
    }
    //
    //    private static final SimpleRpcCodec INSTANCE = new SimpleRpcCodec();
    //
    //    public static RpcCodec getInstance() {
    //        return INSTANCE;
    //    }
    //
    //    @Override
    //    public byte[] toBytes(Object object) {
    //        return JsonBs.serializeBytes(object);
    //    }
    //
    //    @Override
    //    public <T> T toObject(byte[] bytes, Class<T> tClass) {
    //        return JsonBs.deserializeBytes(bytes, tClass);
    //    }
    //
}
