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

import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.api.event.GlobalEventBus;
import com.taotao.cloud.ccsr.api.event.MetadataChangeEvent;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataDeleteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.spi.Join;

/**
 * @author shuigedeng
 * @date 2025-03-24 20:54
 */
@Join
public class DeleteRequestHandler extends AbstractConfigRequestHandler<MetadataDeleteRequest> {

    @Override
    public Response handle(Message request) {

        Log.print("===DeleteRequestHandler【开始】===> request: %s", request);

        MetadataDeleteRequest delete = (MetadataDeleteRequest) request;

        String key =
                storage.key(
                        delete.getNamespace(),
                        delete.getGroup(),
                        delete.getTag(),
                        delete.getDataId());
        if (key == null) {
            return ResponseHelper.error(ResponseCode.PARAM_INVALID.getCode(), "key is null");
        }

        Metadata oldData = storage.get(key);
        if (oldData != null) {
            Metadata removed = storage.delete(key);
            if (removed != null) {
                GlobalEventBus.post(new MetadataChangeEvent(removed, EventType.DELETE));
            }
        }
        Log.print("===DeleteRequestHandler【完成】===> request: %s", request);

        return ResponseHelper.success();
    }

    @Override
    public Class<?> clazz() {
        return MetadataDeleteRequest.class;
    }
}
