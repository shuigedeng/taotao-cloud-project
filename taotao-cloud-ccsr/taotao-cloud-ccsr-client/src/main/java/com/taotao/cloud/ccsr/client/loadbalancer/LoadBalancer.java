package com.taotao.cloud.ccsr.client.loadbalancer;

import com.taotao.cloud.ccsr.client.dto.ServerAddress;
import com.taotao.cloud.ccsr.spi.SPI;

import java.util.List;

@SPI("random")
public interface LoadBalancer {
    ServerAddress select(List<ServerAddress> servers);
}
