package com.taotao.cloud.sys.biz.tools.tcp.service;

import com.taotao.cloud.common.utils.LogUtil;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/server/message")
@Component
public class WebSocketService {
    private static List<Session> sessions = new ArrayList<>();
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        LogUtil.info("打开 session 连接");
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
	    LogUtil.error("发生错误",error);
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message) {
        for (Session session : sessions) {
            if (session != null && session.isOpen()) {
                try {
	                LogUtil.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                    synchronized (session) {
                        session.getBasicRemote().sendText(message);
                    }
                } catch (Exception e) {
	                LogUtil.error("服务端发送消息给客户端失败：{}", e);
                }
            }else{
	            LogUtil.error("session 已经关闭, 发送消息失败:{}",message);
            }
        }

    }
}
