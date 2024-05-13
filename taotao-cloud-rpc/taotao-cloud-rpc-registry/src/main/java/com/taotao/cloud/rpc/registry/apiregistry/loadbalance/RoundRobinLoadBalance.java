package com.taotao.cloud.rpc.registry.apiregistry.loadbalance;

import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮训负载均衡器
 */
public class RoundRobinLoadBalance extends BaseLoadBalance {
    private AtomicLong count=new AtomicLong(0L);
    private final Long MaxCount=Long.MAX_VALUE-100000000000000L;
    @Override
    public String getAvailableHostPort(String appName) {
		long t =count.getAndIncrement();
        if(t>MaxCount){
            count.set(0L);
        }
		List<String> serverList = getAvailableHostPortList(appName);
        if(serverList==null||serverList.size()==0)
            throw new ApiRegistryException(appName+"服务可用列表为空");
        Long index = t%serverList.size();
        return serverList.get(index.intValue());
    }
}
