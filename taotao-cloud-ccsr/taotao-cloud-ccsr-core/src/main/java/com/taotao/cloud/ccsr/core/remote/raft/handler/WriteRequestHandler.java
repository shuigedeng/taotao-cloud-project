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
import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.event.GlobalEventBus;
import com.taotao.cloud.ccsr.api.event.MetadataChangeEvent;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.common.utils.MD5Utils;
import com.taotao.cloud.ccsr.spi.Join;

/**
 * @author shuigedeng
 * @date 2025-03-24 20:52
 */
@Join
public class WriteRequestHandler extends AbstractConfigRequestHandler<MetadataWriteRequest> {

    @Override
    public Response handle(Message request) {

        Log.print("===WriteRequestHandler执行【开始】===> request: %s", request);

        MetadataWriteRequest write = (MetadataWriteRequest) request;
        Metadata newData = write.getMetadata();
        String key = storage.key(newData);
        if (key == null) {
            return ResponseHelper.error(ResponseCode.PARAM_INVALID.getCode(), "key is null");
        }
        Metadata oldData = storage.get(key);

        if (super.put(newData) && oldData != null) {
            if (!MD5Utils.calculateMD5(newData.getContent()).equals(oldData.getMd5())) {
                // MD5对比，有更新再广播推送
                GlobalEventBus.post(new MetadataChangeEvent(newData, EventType.PUT));
            }
        }

        Log.print("===WriteRequestHandler【完成】===> request: %s", request);

        return ResponseHelper.success(Any.pack(newData));
    }

    @Override
    public Class<?> clazz() {
        return MetadataWriteRequest.class;
    }
}
