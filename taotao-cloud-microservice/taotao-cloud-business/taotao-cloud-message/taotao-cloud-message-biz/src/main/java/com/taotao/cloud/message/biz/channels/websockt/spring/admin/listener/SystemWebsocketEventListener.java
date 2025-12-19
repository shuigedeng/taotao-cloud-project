package com.taotao.cloud.message.biz.channels.websockt.spring.admin.listener;

import com.taotao.boot.websocket.spring.admin.message.DictChangeMessage;
import com.taotao.boot.websocket.spring.common.distribute.MessageDO;
import com.taotao.boot.websocket.spring.common.distribute.MessageDistributor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * SystemWebsocketEventListener
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class SystemWebsocketEventListener {

    private final MessageDistributor messageDistributor;

    public SystemWebsocketEventListener( MessageDistributor messageDistributor ) {
        this.messageDistributor = messageDistributor;
    }

    /**
     * 字典修改事件监听
     *
     * @param event the `DictChangeEvent`
     */
    @Async
    @EventListener(DictChangeEvent.class)
    public void onDictChangeEvent( DictChangeEvent event ) {
        // 构建字典修改的消息体
        DictChangeMessage dictChangeMessage = new DictChangeMessage();
        dictChangeMessage.setDictCode(event.getDictCode());
        String msg = JsonUtils.toJson(dictChangeMessage);

        // 广播修改信息
        MessageDO messageDO = new MessageDO().setMessageText(msg).setNeedBroadcast(true);
        messageDistributor.distribute(messageDO);
    }

}
