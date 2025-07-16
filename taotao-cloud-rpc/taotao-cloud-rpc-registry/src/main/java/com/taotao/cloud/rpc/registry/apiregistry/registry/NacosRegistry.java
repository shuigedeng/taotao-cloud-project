/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.rpc.registry.apiregistry.registry;

import com.taotao.cloud.rpc.registry.apiregistry.anno.INacosRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Redis注册中心实现
 */
public class NacosRegistry extends BaseRegistry {

    private Map<String, List<String>> cacheServerList = new HashMap<>();

    public NacosRegistry() {
        super();
    }

    private INacosRegistry getNacosRegistryImpl() {
        //		INacosRegistry o = ContextUtils.getBean(INacosRegistry.class, true);
        //		if (o == null) {
        //			throw new ApiRegistryException("nacos未开启或未加载实现包");
        //		}
        return null;
    }

    @Override
    public void register() {
        super.register();
        getNacosRegistryImpl().register();
        // ThreadUtils.system().submit("apiRegistry nacos心跳获取服务列表", () -> {
        // 	while (!ThreadUtils.system().isShutdown()) {
        // 		try {
        // 			heartBeat();
        // 		} catch (Exception e) {
        // 			LogUtils.error(RedisRegistry.class, ApiRegistryProperties.Project, "nacos心跳获取服务列表出错");
        // 		}
        //
        //	ThreadUtils.sleep(ApiRegistryProperties.getRegistryNacosServerListCacheHeartBeatTime());
        // 	}
        // });
    }

    private void heartBeat() {
        Map<String, List<String>> list = getNacosRegistryImpl().getServerList();
        if (list != null) {
            cacheServerList = list;
        }
    }

    @Override
    public Map<String, List<String>> getServerList() {
        return cacheServerList;
    }

    @Override
    public String getReport() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> kv : cacheServerList.entrySet()) {
            sb.append("服务:" + kv.getKey() + "\r\n");
            for (String server : kv.getValue()) {
                sb.append("    " + server + "\r\n");
            }
        }
        return sb.toString();
    }

    @Override
    public void close() {
        super.close();
        //		INacosRegistry registry = ContextUtils.getBean(INacosRegistry.class, true);
        //		if (registry != null) {
        //			registry.close();
        //		}
    }
}
