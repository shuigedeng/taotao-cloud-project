package com.taotao.cloud.rpc.registry.register.domain.message.impl;

import com.taotao.cloud.rpc.registry.register.domain.message.NotifyMessageHeader;

/**
 * 默认通知消息頭
 * @author shuigedeng
 * @since 2024.06
 */
class DefaultNotifyMessageHeader implements NotifyMessageHeader {

    private static final long serialVersionUID = -5742810870688287022L;

    /**
     * 消息类型
     * @since 2024.06
     */
    private String type;

    @Override
    public String type() {
        return type;
    }

    public DefaultNotifyMessageHeader type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRegisterMessageHeader{" +
                "type=" + type +
                '}';
    }

}
