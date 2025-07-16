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

package com.taotao.cloud.rpc.registry.apiregistry.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载均衡器定义
 */
public class BaseLoadBalance {

    public BaseLoadBalance() {
        // ThreadUtils.system().submit("apiRegistry 故障恢复定时任务", () -> {
        // 	while (!ThreadUtils.system().isShutdown()) {
        // 		if (failsList.size() > 0) {
        // 			synchronized (failsListLock) {
        // 				failsList.clear();
        // 			}
        // 		}
        // 		ThreadUtils.sleep(ApiRegistryProperties.getRegistryFailRetryTimeSpan());
        // 	}
        // });
    }

    /**
     * 可用负载均衡节点
     */
    public String getAvailableHostPort(String appName) {
        return null;
    }

    /**
     * 添加失败节点
     */
    public void addFail(String appName, String hostPort) {
        boolean isConnect = isConnectHostPort(hostPort);
        if (!isConnect) {
            synchronized (failsListLock) {
                if (!failsList.containsKey(appName)) {
                    failsList.put(appName, new ArrayList<>());
                }
                List<String> list = failsList.get(appName);
                if (!list.contains(hostPort)) {
                    list.add(hostPort);
                }
            }
        }
    }

    public static boolean isConnectHostPort(String hostPort) {
        // UriComponents url = UriComponentsBuilder.fromUriString(ApiUtils.getUrl(hostPort,
        // "")).build();
        // return NetworkUtils.isHostConnectable(url.getHost(), url.getPort() <= 0 ? 80 :
        // url.getPort(), 5000);
        return true;
    }

    protected ConcurrentHashMap<String, List<String>> failsList = new ConcurrentHashMap<>();
    protected Object failsListLock = new Object();

    protected List<String> getAvailableHostPortList(String appName) {
        List rs = new ArrayList<String>();
        // 配置优先
        //		for (String s : ApiRegistryProperties.getAppNameHosts(appName)) {
        ////			if (!StringUtils.isEmpty(s)) {
        ////				rs.add(s);
        ////			}
        //		}
        if (rs.size() > 0) {
            return rs;
        }
        // 注册中心
        //		BaseRegistry registry = ContextUtils.getBean(BaseRegistry.class, false);
        //		if (registry != null) {
        //			List<String> list = registry.getServerList().get(appName);
        //			if (list != null) {
        //				for (String s : list) {
        //					if (!StringUtils.isEmpty(s)) {
        //						rs.add(s);
        //					}
        //				}
        //			}
        //		}
        // 移除错误
        List<String> fails = failsList.get(appName);
        if (fails != null) {
            rs.removeAll(fails);
        }
        return rs;
    }

    public static void main(String[] args) {
        String[] ports =
                new String[] {
                    "10.70.0.91:8080",
                    "http://10.70.0.91:8080/aaa",
                    "http://www.baidu.com/aa",
                    "http://inner-114.linkmore.com/",
                    "inner-114.linkmore.com"
                };
        for (int i = 0; i < 1000; i++) {
            for (String p : ports) {
                //				Chooser.Ref<Boolean> r = new Ref<>(false);
                // long time = TimeWatchUtils.time(() -> {
                // 	r.setData(BaseLoadBalance.isConnectHostPort(p));
                // });
                //
                // System.out.println(p + " -> " + r + " :time " + time);
            }
        }
    }
}
