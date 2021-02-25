package com.taotao.cloud.mqtt.demo;

import java.util.Map;
import java.util.Set;

/**
 * 客户端上下线事件存储器，实际生产环境建议使用数据库或者 Redis 持久化版本实现。
 * 如果使用 Redis，可以使用多级hash 表，第一级key 是 clientId，第二级 key 是 channelId 第三级是 eventType
 * 如果使用数据库，唯一键为clientId+channelId+eventType
 */
public interface MqttClientStatusStore {
    /**
     * 存储客户端指定 channel 的指定类型的上下线事件
     *
     * @param clientId
     * @param channelId
     * @param eventType
     * @param event
     */
    void addEvent(String clientId, String channelId, String eventType, ClientStatusEvent event);

    /**
     * 删除客户端指定 channel的上下线事件
     *
     * @param clientId
     * @param channelId
     */
    void deleteEvent(String clientId, String channelId);

    /**
     * 读取客户端指定 channel的上下线事件
     *
     * @param clientId
     * @param channelId
     * @return
     */
    Set<ClientStatusEvent> getEvent(String clientId, String channelId);

    /**
     * 根据 clientId 读取所有相关的上下线事件
     * @param clientId
     * @return
     */
    Map<String, Set<ClientStatusEvent>> getEventsByClientId(String clientId);
}
