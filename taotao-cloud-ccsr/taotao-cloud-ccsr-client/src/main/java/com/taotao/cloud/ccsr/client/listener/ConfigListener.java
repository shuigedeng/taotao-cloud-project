package com.taotao.cloud.ccsr.client.listener;

import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.spi.SPI;

@SPI
public interface ConfigListener<T extends ConfigData> {

    void receive(String dataStr, T data, EventType eventType);

    void register();
}
