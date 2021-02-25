package com.taotao.cloud.mqtt.demo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 简易的内存版状态存储器，不可用于生产环境
 */
public class MemoryHashMapStoreImpl implements MqttClientStatusStore {
    final Map<String /*clientId*/, Map<String /*channelId*/, Set<ClientStatusEvent>>> clientEventMap = new HashMap<>();

    @Override
    public synchronized void addEvent(String clientId, String channelId, String eventType, ClientStatusEvent event) {
        clientEventMap.putIfAbsent(clientId, new HashMap<>());
        Map<String /*channelId*/, Set<ClientStatusEvent>> channelMap = clientEventMap.get(clientId);
        channelMap.putIfAbsent(channelId, new HashSet<>());
        Set<ClientStatusEvent> eventSet = channelMap.get(channelId);
        eventSet.add(event);
    }

    @Override
    public synchronized void deleteEvent(String clientId, String channelId) {
        Map<String /*channelId*/, Set<ClientStatusEvent>> channelMap = clientEventMap.get(clientId);
        if (channelMap == null) {
            return;
        }
        channelMap.remove(channelId);
        if (channelMap.isEmpty()) {
            clientEventMap.remove(clientId);
        }
    }

    @Override
    public Set<ClientStatusEvent> getEvent(String clientId, String channelId) {
        Map<String /*channelId*/, Set<ClientStatusEvent>> channelMap = clientEventMap.get(clientId);
        if (channelMap == null) {
            return null;
        }
        return channelMap.get(channelId);
    }

    @Override
    public Map<String, Set<ClientStatusEvent>> getEventsByClientId(String clientId) {
        return clientEventMap.get(clientId);
    }
}
