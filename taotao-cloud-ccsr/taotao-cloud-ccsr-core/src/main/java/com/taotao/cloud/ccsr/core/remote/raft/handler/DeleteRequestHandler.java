package com.taotao.cloud.ccsr.core.remote.raft.handler;

import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataDeleteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.event.GlobalEventBus;
import com.taotao.cloud.ccsr.api.event.MetadataChangeEvent;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.spi.Join;
import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.log.Log;

/**
 * @author SpringCat
 * @date 2025-03-24 20:54
 */
@Join
public class DeleteRequestHandler extends AbstractConfigRequestHandler<MetadataDeleteRequest> {

    @Override
    public Response handle(Message request) {

        Log.print("===DeleteRequestHandler【开始】===> request: %s", request);

        MetadataDeleteRequest delete = (MetadataDeleteRequest) request;

        String key = storage.key(delete.getNamespace(), delete.getGroup(), delete.getTag(), delete.getDataId());
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
