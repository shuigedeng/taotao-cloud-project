package com.taotao.cloud.ccsr.client.loadbalancer;

import com.taotao.cloud.ccsr.spi.Join;
import org.ohara.msc.dto.ServerAddress;

import java.util.List;
import java.util.Random;

@Join
public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();

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

        return activeServers.get(random.nextInt(activeServers.size()));
    }
}
