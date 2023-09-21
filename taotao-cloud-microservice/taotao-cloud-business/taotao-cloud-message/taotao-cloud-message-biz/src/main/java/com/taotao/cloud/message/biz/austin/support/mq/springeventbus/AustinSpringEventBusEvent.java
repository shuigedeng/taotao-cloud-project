package com.taotao.cloud.message.biz.austin.support.mq.springeventbus;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 描述：消息
 *
 * @author tony
 * @since 2023/2/6 19:59
 */
@Getter
public class AustinSpringEventBusEvent extends ApplicationEvent {

    private AustinSpringEventSource austinSpringEventSource;

    public AustinSpringEventBusEvent(Object source, AustinSpringEventSource austinSpringEventSource) {
        super(source);
        this.austinSpringEventSource = austinSpringEventSource;
    }

}
