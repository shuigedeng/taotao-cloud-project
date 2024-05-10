package com.taotao.cloud.ttcrpc.registry.apiregistry;

public interface CoreRequestInterceptor {
    void append(RequestInfo request);
}
