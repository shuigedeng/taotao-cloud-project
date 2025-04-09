package com.taotao.cloud.ccsr.client.loadbalancer;

import com.taotao.cloud.ccsr.spi.SPI;
import com.taotao.cloud.ccsr.dto.ServerAddress;

import java.util.List;

@SPI("random")
public interface LoadBalancer {
    ServerAddress select(List<ServerAddress> servers);
}
