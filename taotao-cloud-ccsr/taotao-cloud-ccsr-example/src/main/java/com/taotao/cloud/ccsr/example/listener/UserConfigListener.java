package com.taotao.cloud.ccsr.example.listener;
import com.taotao.cloud.ccsr.client.listener.AbstractConfigListener;
import com.taotao.cloud.ccsr.example.dto.User;

import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.spi.Join;

import java.util.List;

@Join
public class UserConfigListener extends AbstractConfigListener<User> {
    @Override
    public void receive(String dataStr, User data, EventType eventType) {
        // TODO: Implement the logic to handle the received data
        Log.print("客户端收到配置变更推送: eventType=%s, dataStr=%s", eventType, dataStr);
    }

}
