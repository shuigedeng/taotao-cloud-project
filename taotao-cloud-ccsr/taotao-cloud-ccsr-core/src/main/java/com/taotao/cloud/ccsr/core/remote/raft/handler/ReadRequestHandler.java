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

package com.taotao.cloud.ccsr.core.remote.raft.handler;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.spi.Join;

/**
 * @author shuigedeng
 * @date 2025-03-24 20:53
 */
@Join
public class ReadRequestHandler extends AbstractConfigRequestHandler<MetadataReadRequest> {

    @Override
    public Response handle(Message request) {
        MetadataReadRequest read = (MetadataReadRequest) request;
        String key =
                storage.key(read.getNamespace(), read.getGroup(), read.getTag(), read.getDataId());
        if (key == null) {
            return ResponseHelper.success("key is null");
        }
        Metadata data = storage.get(key);
        if (data == null) {
            return ResponseHelper.success("data not exist, key=" + key);
        }
        return ResponseHelper.success(Any.pack(data));
    }

    @Override
    public Class<?> clazz() {
        return MetadataReadRequest.class;
    }
}
