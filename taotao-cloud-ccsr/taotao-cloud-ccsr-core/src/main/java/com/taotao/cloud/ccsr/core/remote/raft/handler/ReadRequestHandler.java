package com.taotao.cloud.ccsr.core.remote.raft.handler;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.spi.Join;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;

/**
 * @author shuigedeng
 * @date 2025-03-24 20:53
 */
@Join
public class ReadRequestHandler extends AbstractConfigRequestHandler<MetadataReadRequest> {

    @Override
    public Response handle(Message request) {
        MetadataReadRequest read = (MetadataReadRequest) request;
        String key = storage.key(read.getNamespace(), read.getGroup(), read.getTag(), read.getDataId());
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
