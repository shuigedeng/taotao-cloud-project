package com.taotao.cloud.sys.biz.modules.core.service.connect.events;

import org.springframework.context.ApplicationEvent;

/**
 * 连接删除事件
 */
public class DeleteSecurityConnectEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DeleteSecurityConnectEvent(Object source) {
        super(source);
    }
}
