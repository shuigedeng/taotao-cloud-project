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

package com.taotao.cloud.ccsr.core.utils;

import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataDeleteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.core.serializer.Serializer;

import java.util.Arrays;
import java.util.List;

/**
 * ProtoMessageUtils
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ProtoMessageUtils {

    public static Message parse( Serializer serializer, byte[] bytes ) {
        List<Class<? extends Message>> messages =
                List.of(
                        MetadataReadRequest.class,
                        MetadataWriteRequest.class,
                        MetadataDeleteRequest.class);

        for (Class<? extends Message> message : messages) {
            try {
                return serializer.deserialize(bytes, message);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Message解析失败，bytes: " + Arrays.toString(bytes));
    }
}
