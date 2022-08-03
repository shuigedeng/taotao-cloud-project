package com.taotao.cloud.sys.biz.api.controller.tools.tcp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ServerEndpoint(value = "/server/message")
@Component
public class WebSocketService {
    private static List<Session> sessions = new ArrayList<>();
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("打开 session 连接");
        sessions.add(session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误",error);
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message) {
        for (Session session : sessions) {
            if (session != null && session.isOpen()) {
                try {
                    log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                    synchronized (session) {
                        session.getBasicRemote().sendText(message);
                    }
                } catch (Exception e) {
                    log.error("服务端发送消息给客户端失败：{}", e);
                }
            }else{
                log.error("session 已经关闭, 发送消息失败:{}",message);
            }
        }

    }
}
