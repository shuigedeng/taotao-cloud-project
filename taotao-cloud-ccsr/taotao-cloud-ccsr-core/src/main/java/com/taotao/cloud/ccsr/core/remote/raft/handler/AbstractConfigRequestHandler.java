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
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.common.enums.RaftGroup;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.exception.CcsrException;
import com.taotao.cloud.ccsr.core.storage.MetadaStorage;
import com.taotao.cloud.ccsr.core.utils.StorageHolder;

/**
 * @author shuigedeng
 */
public abstract class AbstractConfigRequestHandler<T extends Message> implements RequestHandler<T> {

    protected final MetadaStorage storage = StorageHolder.getInstance("metadata");

    public abstract Class<?> clazz();

    public Metadata get(String key) {
        return storage.get(key);
    }

    public boolean put(Metadata metadata) {
        return storage.put(metadata) != null;
    }

    @Override
    public String group() {
        return RaftGroup.CONFIG_CENTER_GROUP.getName();
    }

    @Override
    public Response onApply(Message request) {
        try {
            return handle(request);
        } catch (Exception e) {
            return ResponseHelper.error(
                    ResponseCode.SYSTEM_ERROR.getCode(), "Processing failed: " + e.getMessage());
        }
    }

    @Override
    public void onError(Throwable error) {
        throw new CcsrException(error);
    }

    public abstract Response handle(Message request);
}
