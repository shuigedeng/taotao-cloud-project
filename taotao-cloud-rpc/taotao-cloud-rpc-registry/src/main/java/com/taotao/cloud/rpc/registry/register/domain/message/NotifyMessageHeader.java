package com.taotao.cloud.rpc.registry.register.domain.message;

import com.taotao.cloud.rpc.registry.register.simple.constant.MessageTypeConst;

import java.io.Serializable;

/**
 * 通知消息头
 * @author shuigedeng
 * @since 2024.06
 */
public interface NotifyMessageHeader extends Serializable {

    /**
     * 消息类型
     * @return 消息类型
     * @since 2024.06
     * @see MessageTypeConst 类型常量
     */
    String type();

}
