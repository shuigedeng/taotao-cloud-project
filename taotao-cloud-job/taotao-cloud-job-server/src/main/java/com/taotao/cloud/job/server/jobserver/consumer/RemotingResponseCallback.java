package com.taotao.cloud.job.server.jobserver.consumer;

import com.taotao.cloud.server.consumer.entity.Response;

public interface RemotingResponseCallback {
    void callback(Response response);
}
