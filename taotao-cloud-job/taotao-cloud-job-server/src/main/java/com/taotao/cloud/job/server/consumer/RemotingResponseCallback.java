package com.taotao.cloud.job.server.consumer;


import com.taotao.cloud.job.server.consumer.entity.Response;

public interface RemotingResponseCallback {
    void callback(Response response);
}
