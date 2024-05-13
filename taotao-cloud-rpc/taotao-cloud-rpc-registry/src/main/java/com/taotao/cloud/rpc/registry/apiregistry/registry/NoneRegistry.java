package com.taotao.cloud.rpc.registry.apiregistry.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 空注册中心
 */
public class NoneRegistry extends BaseRegistry {
    private Map<String, List<String>> cacheServerList=new HashMap<>();
    public NoneRegistry() {
    }

    @Override
    public Map<String, List<String>> getServerList() {
        return cacheServerList;
    }
}
