package com.taotao.cloud.ccsr.core.remote.raft.handler;

import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.core.storage.MetadaStorage;
import com.taotao.cloud.ccsr.core.utils.StorageHolder;
import org.ohara.msc.common.enums.RaftGroup;
import org.ohara.msc.common.enums.ResponseCode;
import org.ohara.msc.common.exception.OHaraMcsException;

/**
 * @author SpringCat
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
            return ResponseHelper.error(ResponseCode.SYSTEM_ERROR.getCode(), "Processing failed: " + e.getMessage());
        }
    }

    @Override
    public void onError(Throwable error) {
        throw new OHaraMcsException(error);
    }

    public abstract Response handle(Message request);
}
