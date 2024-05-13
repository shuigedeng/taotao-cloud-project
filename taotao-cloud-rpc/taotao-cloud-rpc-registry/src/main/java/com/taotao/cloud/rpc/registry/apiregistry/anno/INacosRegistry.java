package com.taotao.cloud.rpc.registry.apiregistry.anno;

import java.util.List;
import java.util.Map;

public interface INacosRegistry {
     void register();
    /**获取服务列表*/
     Map<String, List<String>> getServerList();
     void close();
}
