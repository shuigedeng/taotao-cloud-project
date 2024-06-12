package com.taotao.cloud.rpc.registry.register.domain.message.impl;

import com.taotao.cloud.rpc.registry.register.domain.message.NotifyMessage;
import com.taotao.cloud.rpc.registry.register.domain.message.NotifyMessageHeader;

/**
 * 通知消息工具类
 * @author shuigedeng
 * @since 2024.06
 */
public final class NotifyMessages {

    private NotifyMessages(){}

    /**
     * 初始化消息信息
     * @param type 类型
     * @param body 消息体
     * @return 注册消息
     * @since 2024.06
     */
    public static NotifyMessage of(final String type,
                                   final Object body) {
//        String seqId = Ids.uuid32();
        return of(type, "seqId", body);
    }

    /**
     * 初始化消息信息
     * @param type 类型
     * @param seqId 消息标识
     * @param body 消息体
     * @return 注册消息
     * @since 2024.06
     */
    public static NotifyMessage of(final String type,
                                   final String seqId,
                                   final Object body) {
        DefaultNotifyMessage registerMessage = new DefaultNotifyMessage();
        DefaultNotifyMessageHeader messageHeader = new DefaultNotifyMessageHeader();
        messageHeader.type(type);

        registerMessage.seqId(seqId);
        registerMessage.header(messageHeader);
        registerMessage.body(body);
        return registerMessage;
    }

    /**
     * 获取消息的类型
     * @param notifyMessage 注册消息
     * @return 消息类型
     * @since 2024.06
     */
    public static String type(final NotifyMessage notifyMessage) {
        NotifyMessageHeader header = header(notifyMessage);
        return header.type();
    }

    /**
     * 获取消息头
     * @param notifyMessage 消息
     * @return 消息头
     * @since 2024.06
     */
    private static NotifyMessageHeader header(final NotifyMessage notifyMessage) {
//        ArgUtil.notNull(notifyMessage, "registerMessage");
        NotifyMessageHeader messageHeader = notifyMessage.header();
//        ArgUtil.notNull(messageHeader, "messageHeader");
        return messageHeader;
    }

}
