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

import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮训负载均衡器
 */
public class RoundRobinLoadBalance extends BaseLoadBalance {
    private AtomicLong count = new AtomicLong(0L);
    private final Long MaxCount = Long.MAX_VALUE - 100000000000000L;

    @Override
    public String getAvailableHostPort(String appName) {
        long t = count.getAndIncrement();
        if (t > MaxCount) {
            count.set(0L);
        }
        List<String> serverList = getAvailableHostPortList(appName);
        if (serverList == null || serverList.size() == 0)
            throw new ApiRegistryException(appName + "服务可用列表为空");
        Long index = t % serverList.size();
        return serverList.get(index.intValue());
    }
}
