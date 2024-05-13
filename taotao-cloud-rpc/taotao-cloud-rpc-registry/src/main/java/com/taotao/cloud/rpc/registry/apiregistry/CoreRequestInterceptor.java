package com.taotao.cloud.rpc.registry.apiregistry;

public interface CoreRequestInterceptor {
    void append(RequestInfo request);
}
