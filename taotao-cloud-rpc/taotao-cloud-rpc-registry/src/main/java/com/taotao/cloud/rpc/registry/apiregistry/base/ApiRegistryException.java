package com.taotao.cloud.rpc.registry.apiregistry.base;


public class ApiRegistryException extends RuntimeException {
    public ApiRegistryException(String message,Exception exp){
        super(message, exp);
    }
    public ApiRegistryException(String message){
        super(message);
    }
    public ApiRegistryException(Exception exp){
        super(exp);
    }
}
