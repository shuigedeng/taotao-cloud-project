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

package com.taotao.cloud.jetcache.enhance.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义 AtomicIntegerSerializer
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 09:13:01
 */
public class AtomicIntegerSerializer extends Serializer<AtomicInteger> {

    @Override
    public void write(Kryo kryo, Output output, AtomicInteger object) {
        output.writeString(String.valueOf(object.get()));
    }

    @Override
    public AtomicInteger read(Kryo kryo, Input input, Class<? extends AtomicInteger> type) {
        return new AtomicInteger(input.readInt());
    }
}
