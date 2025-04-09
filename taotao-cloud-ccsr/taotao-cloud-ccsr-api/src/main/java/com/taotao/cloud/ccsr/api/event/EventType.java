package com.taotao.cloud.ccsr.api.event;

public enum EventType {
    PUT,
    GET,
    DELETE,
    RELOAD;

    public boolean isPut() {
        return PUT.equals(this);
    }

    public boolean isGet() {
        return GET.equals(this);
    }

    public boolean isDelete() {
        return DELETE.equals(this);
    }

    public boolean isReload() {
        return RELOAD.equals(this);
    }
}
