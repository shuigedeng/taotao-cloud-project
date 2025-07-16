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

import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import redis.clients.jedis.Jedis;

/**
 * Redis注册中心实现
 */
public class RedisRegistry extends BaseRegistry {
    Jedis jedis;
    // key前缀
    private final String KEY_PREFIX = "env" + "@" + "com.taotao.cloud.rpc.registry.apiregistry";
    // 当前实例标识
    private final String CLIENT_ID = UUID.randomUUID().toString().replace("-", "");
    private Map<String, List<String>> cacheServerList = new HashMap<>();
    private boolean running = false;
    private Object lock = new Object();

    public RedisRegistry() {
        super();
        //        String hostPort = ApiRegistryProperties.getRegistryRedisHost();
        //        if (StringUtils.isEmpty(hostPort)) {
        //            throw new ApiRegistryException("redis地址不能为空");
        //        }
        //        jedis = new Jedis(hostPort.split(":")[0],
        // ConvertUtils.convert(hostPort.split(":")[1], Integer.class));
    }

    @Override
    public void register() {
        super.register();
        // ThreadUtils.system().submit("apiRegistry redis心跳保持", () -> {
        //     while (!ThreadUtils.system().isShutdown()) {
        //         Long timeSpan = TimeWatchUtils.time(()->{
        //             try {
        //                 heartBeat();
        //             } catch (Exception e) {
        //                 LogUtils.error(RedisRegistry.class, ApiRegistryProperties.Project,
        // "redis心跳出错");
        //             }
        //         });
        //         int sleepTime =
        // ApiRegistryProperties.getRegistryRedisHeartBeatTime()-timeSpan.intValue();
        //         ThreadUtils.sleep(sleepTime>0?sleepTime:0);
        //     }
        // });
        running = true;
    }

    @Override
    public Map<String, List<String>> getServerList() {
        return cacheServerList;
    }

    @Override
    public String getReport() {
        synchronized (lock) {
            refreshServerList();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, List<String>> kv : cacheServerList.entrySet()) {
                sb.append("服务:" + kv.getKey() + "\r\n");
                for (String server : kv.getValue()) {
                    sb.append("    " + server + "\r\n");
                }
            }
            return sb.toString();
        }
    }

    private void heartBeat() {
        synchronized (lock) {
            if (running && jedis != null) {
                // 检测重连
                checkRedisConnected();
                // 同步心跳
                // if (!StringUtils.isEmpty(WebUtils.getHost())) {
                //     if(ApiRegistryProperties.getRegistryClientRegistered()) {
                //         jedis.set(getClientKey(), WebUtils.getHost(),
                // SetParams.setParams().ex(ApiRegistryProperties.getRegistryRedisExpireTime()));
                //     }
                // }
                // 刷新本地缓存列表
                refreshServerList();
            }
        }
    }

    private void checkRedisConnected() {
        if (jedis != null) {
            try {
                if ("PONG".equals(jedis.ping())) {
                    return;
                }
                throw new ApiRegistryException("redis 连接状态异常");
            } catch (Exception e) {
                jedis.disconnect();
                try {
                    jedis.connect();
                    jedis.ping();
                } catch (Exception e2) {
                    throw new ApiRegistryException("redis 断线重连失败", e2);
                }
            }
        }
    }

    private synchronized void refreshServerList() {
        HashMap<String, List<String>> serverList = new HashMap<>();
        Set<String> jkeys = jedis.keys(KEY_PREFIX + "*");
        if (jkeys != null && !jkeys.isEmpty()) {
            String[] keys = jkeys.toArray(new String[0]);
            List<String> values = jedis.mget(keys);
            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                String hostPort = values.get(i);
                //                if (StringUtils.isEmpty(hostPort)) {
                //                    continue;
                //                }
                String appName = getAppNameFromKey(key);
                if (!serverList.containsKey(appName)) {
                    serverList.put(appName, new ArrayList<>());
                }
                serverList.get(appName).add(hostPort);
            }
        }
        cacheServerList = serverList;
    }

    /**组合获取key*/
    private String getClientKey() {
        return KEY_PREFIX + "@" + "spring.appli.name" + "@" + CLIENT_ID;
    }

    /**从key中解析获取appName*/
    private String getAppNameFromKey(String key) {
        String[] infos = key.split("@");
        return infos[2];
    }

    @Override
    public void close() {
        running = false;
        super.close();
        synchronized (lock) {
            if (jedis != null) {
                jedis.del(getClientKey()); // 列表
                jedis.close();
                jedis = null;
            }
        }
    }
}
