package com.taotao.cloud.rpc.registry.apiregistry.rpcclient;

import com.taotao.cloud.rpc.registry.apiregistry.RequestInfo;
import java.lang.reflect.Type;

/**
 * IRpcClient 客户端
 */
public interface IRpcClient {
    <T>T execute(RequestInfo requestInfo, Type cls);
}
