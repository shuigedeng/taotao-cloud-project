package com.taotao.cloud.redis.delay.consts;

/**
 * ListenerType
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:25:11
 */
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
