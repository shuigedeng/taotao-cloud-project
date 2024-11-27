package com.taotao.cloud.job.server.jobserver.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.taotao.cloud.remote.protos.MqCausa;

import java.util.concurrent.CompletableFuture;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlushRequest {
    MqCausa.Message message;
    CompletableFuture<Response> future;

    public void complete(){
        future.complete(new Response(ResponseEnum.SUCCESS));
    }
    public void flushFail(){
        future.complete(new Response(ResponseEnum.FLUSH_ERROR));
    }

}
