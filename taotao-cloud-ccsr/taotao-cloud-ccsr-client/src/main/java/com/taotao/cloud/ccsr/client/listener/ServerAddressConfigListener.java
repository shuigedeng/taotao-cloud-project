package com.taotao.cloud.ccsr.client.listener;

import com.taotao.cloud.ccsr.api.event.EventType;
import org.ohara.msc.common.log.Log;
import org.ohara.msc.dto.ServerAddress;

public class ServerAddressConfigListener extends AbstractConfigListener<ServerAddress> {
    @Override
    public void receive(String dataStr, ServerAddress data, EventType eventType) {
        // TODO: Implement the logic to handle the received ServerAddress configuration
        Log.print("[INNER]ServerAddressConfigListener->客户端收到配置变更: eventType=%s, data=%s", eventType, dataStr);
    }
}
