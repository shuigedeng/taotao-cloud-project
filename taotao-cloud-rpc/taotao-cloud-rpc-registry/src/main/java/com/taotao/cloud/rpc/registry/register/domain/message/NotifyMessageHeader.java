package com.github.houbb.rpc.register.domain.message;

import com.github.houbb.rpc.register.simple.constant.MessageTypeConst;

import java.io.Serializable;

/**
 * 通知消息头
 * @author shuigedeng
 * @since 0.0.8
 */
public interface NotifyMessageHeader extends Serializable {

    /**
     * 消息类型
     * @return 消息类型
     * @since 0.0.8
     * @see MessageTypeConst 类型常量
     */
    String type();

}
