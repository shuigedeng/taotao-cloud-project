package com.taotao.cloud.ccsr.core.remote.raft.handler;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.event.GlobalEventBus;
import com.taotao.cloud.ccsr.api.event.MetadataChangeEvent;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.spi.Join;
import com.taotao.cloud.ccsr.api.event.EventType;
import org.ohara.msc.common.enums.ResponseCode;
import org.ohara.msc.common.log.Log;
import org.ohara.msc.common.utils.MD5Utils;

/**
 * @author SpringCat
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
