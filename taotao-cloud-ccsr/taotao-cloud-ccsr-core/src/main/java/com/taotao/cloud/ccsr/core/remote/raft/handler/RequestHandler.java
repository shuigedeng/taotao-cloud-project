package com.taotao.cloud.ccsr.core.remote.raft.handler;

import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.spi.SPI;

/**
 * 用于Raft接收请求后的处理逻辑，不是RPC服务哈～
 * @author shuigedeng
 */
@SPI
public interface RequestHandler<T extends Message> {

    Response onApply(Message request);

    void onError(Throwable error);

    String group();

    Class<?> clazz();
}
