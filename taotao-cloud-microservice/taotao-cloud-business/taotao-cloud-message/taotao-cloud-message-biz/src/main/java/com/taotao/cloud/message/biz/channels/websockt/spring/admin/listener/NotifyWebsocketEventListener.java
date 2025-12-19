package com.taotao.cloud.message.biz.channels.websockt.spring.admin.listener;

import com.taotao.cloud.message.biz.ballcat.common.websocket.distribute.MessageDistributor;
import com.taotao.boot.websocket.spring.common.distribute.MessageDO;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * NotifyWebsocketEventListener
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class NotifyWebsocketEventListener {

    private final MessageDistributor messageDistributor;

    private final NotifyInfoDelegateHandler<? super NotifyInfo> notifyInfoDelegateHandler;

    /**
     * 公告关闭事件监听
     *
     * @param event the AnnouncementCloseEvent
     */
    @Async
    @EventListener(AnnouncementCloseEvent.class)
    public void onAnnouncementCloseEvent( AnnouncementCloseEvent event ) {
        // 构建公告关闭的消息体
        AnnouncementCloseMessage message = new AnnouncementCloseMessage();
        message.setId(event.getId());
        String msg = JsonUtils.toJson(message);

        // 广播公告关闭信息
        MessageDO messageDO = new MessageDO().setMessageText(msg).setNeedBroadcast(true);
        messageDistributor.distribute(messageDO);
    }

    /**
     * 站内通知推送事件
     *
     * @param event the StationNotifyPushEvent
     */
    @Async
    @EventListener(StationNotifyPushEvent.class)
    public void onAnnouncementPublishEvent( StationNotifyPushEvent event ) {
        NotifyInfo notifyInfo = event.getNotifyInfo();
        List<SysUser> userList = event.getUserList();
        notifyInfoDelegateHandler.handle(userList, notifyInfo);
    }

}
