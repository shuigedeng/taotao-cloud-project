package com.taotao.cloud.rpc.registry.apiregistry.registry;

import com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties;
import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;

public class RegistryFactory {
    public static BaseRegistry create(){
//		String type = ApiRegistryProperties.getRegistryType();
		String type = "";
        if(RedisRegistry.class.getSimpleName().equalsIgnoreCase(type)){
            return new RedisRegistry();
        }
        if(NacosRegistry.class.getSimpleName().equalsIgnoreCase(type)){
            return new NacosRegistry();
        }
        if(NoneRegistry.class.getSimpleName().equalsIgnoreCase(type)){
            return new NoneRegistry();
        }
        throw new ApiRegistryException("请配置ttc.apiRegistry.registry.type");
    }
}
