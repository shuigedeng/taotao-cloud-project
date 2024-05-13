package com.taotao.cloud.ttcrpc.registry.apiregistry.loadbalance;

import com.taotao.cloud.rpc.registry.apiregistry.ApiRegistryProperties;
import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;

public class LoadBalanceFactory {
    public static BaseLoadBalance create(){
        if(RoundRobinLoadBalance.class.getSimpleName().equalsIgnoreCase(ApiRegistryProperties.getRegistryLoadBalanceType())){
            return new RoundRobinLoadBalance();
        }
        throw new ApiRegistryException("请配置bsf.apiRegistry.loadBalance.type");
    }
}
