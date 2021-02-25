package com.taotao.cloud.mqtt.demo;

import java.util.Objects;

public class ClientStatusEvent {
    private String channelId;
    private String clientIp;
    private String eventType;
    private Long time;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientStatusEvent event = (ClientStatusEvent) o;
        return Objects.equals(channelId, event.channelId) &&
            Objects.equals(eventType, event.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelId, eventType);
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isOnlineEvent() {
        if ("connect".equals(eventType)) {
            return true;
        }
        return false;
    }
}
