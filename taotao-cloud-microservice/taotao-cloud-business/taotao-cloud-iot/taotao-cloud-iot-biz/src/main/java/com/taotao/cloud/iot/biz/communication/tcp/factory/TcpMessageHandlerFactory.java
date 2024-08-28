package com.taotao.cloud.iot.biz.communication.tcp.factory;

import lombok.RequiredArgsConstructor;
import net.maku.iot.communication.tcp.handler.TCPMessageHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TCP消息处理器工厂，自动获取所有实现的处理器实例
 *
 * @author LSF maku_lsf@163.com
 */
@Component
@RequiredArgsConstructor
public class TcpMessageHandlerFactory {
    private final ApplicationContext applicationContext;

    /**
     * 所有消息处理器
     */
    private List<TCPMessageHandler> messageHandlers;

    private List<TCPMessageHandler> loadHandlers() {
        if (messageHandlers != null) {
            return messageHandlers;
        }
        messageHandlers = new ArrayList<>(applicationContext.getBeansOfType(TCPMessageHandler.class).values());
        return messageHandlers;
    }

    /**
     * 获取与主题对应的tcp消息处理器
     *
     * @param topic 主题
     * @return 处理器列表
     */
    public List<TCPMessageHandler> getHandlersForTopic(String topic) {
        return Collections.unmodifiableList(loadHandlers().stream()
                .filter(handler -> handler.supports(topic))
                .collect(Collectors.toList()));
    }
}
