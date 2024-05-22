package com.taotao.cloud.rpc.registry.apiregistry.rpcclient;

import com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties;
import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;

public class RpcClientFactory {
    public static IRpcClient create(){
//        if(HttpUrlConnectionRpcClient.class.getSimpleName().equalsIgnoreCase(ApiRegistryProperties.getRpcClientType())){
//            return new HttpUrlConnectionRpcClient();
//        }
//        if(HttpClientRpcClient.class.getSimpleName().equalsIgnoreCase(ApiRegistryProperties.getRpcClientType())){
//            return new HttpClientRpcClient();
//        }
        throw new ApiRegistryException("请配置ttc.apiRegistry.httpClient.type");
    }
}
