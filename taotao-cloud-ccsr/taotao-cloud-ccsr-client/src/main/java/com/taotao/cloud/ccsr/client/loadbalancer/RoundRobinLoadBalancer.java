package com.taotao.cloud.ccsr.client.loadbalancer;

import com.taotao.cloud.ccsr.spi.Join;
import org.ohara.msc.dto.ServerAddress;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Join
public class RoundRobinLoadBalancer implements LoadBalancer {
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServerAddress select(List<ServerAddress> servers) {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No available servers");
        }

        List<ServerAddress> activeServers = servers.stream()
            .filter(ServerAddress::isActive)
            .toList();

        if (activeServers.isEmpty()) {
            throw new IllegalStateException("No active servers available");
        }

        int nextIndex = currentIndex.getAndUpdate(
            idx -> (idx + 1) % activeServers.size()
        );

        return activeServers.get(nextIndex);
    }
}
