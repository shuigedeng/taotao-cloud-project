package com.taotao.cloud.redis.delay.consts;


public enum ListenerType {
    /**
     * take one message once
     */
    SIMPLE,
    /**
     * take list message once
     */
    BATCH
}
