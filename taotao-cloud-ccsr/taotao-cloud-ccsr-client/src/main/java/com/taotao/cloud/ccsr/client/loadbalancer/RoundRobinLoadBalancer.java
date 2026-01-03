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

package com.taotao.cloud.ccsr.client.loadbalancer;

import com.taotao.cloud.ccsr.client.dto.ServerAddress;
import com.taotao.cloud.ccsr.spi.Join;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RoundRobinLoadBalancer
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Join
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServerAddress select( List<ServerAddress> servers ) {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No available servers");
        }

        List<ServerAddress> activeServers =
                servers.stream().filter(ServerAddress::isActive).toList();

        if (activeServers.isEmpty()) {
            throw new IllegalStateException("No active servers available");
        }

        int nextIndex = currentIndex.getAndUpdate(idx -> ( idx + 1 ) % activeServers.size());

        return activeServers.get(nextIndex);
    }
}
