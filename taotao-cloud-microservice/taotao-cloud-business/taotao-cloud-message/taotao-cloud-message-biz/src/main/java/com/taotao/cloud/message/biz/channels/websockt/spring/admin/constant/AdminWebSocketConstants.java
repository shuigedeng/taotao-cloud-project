package com.taotao.cloud.message.biz.channels.websockt.spring.admin.constant;

/**
 * AdminWebSocketConstants
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public final class AdminWebSocketConstants {

    private AdminWebSocketConstants() {
    }

    /**
     * 存储在 WebSocketSession Attribute 中的 token 属性名
     */
    public static final String TOKEN_ATTR_NAME = "access_token";

    /**
     * 存储在 WebSocketSession Attribute 中的 用户唯一标识 属性名
     */
    public static final String USER_KEY_ATTR_NAME = "userId";

}
