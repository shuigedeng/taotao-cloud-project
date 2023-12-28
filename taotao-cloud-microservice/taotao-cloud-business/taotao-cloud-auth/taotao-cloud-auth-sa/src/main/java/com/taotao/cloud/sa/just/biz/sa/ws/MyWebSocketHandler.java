/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sa.just.biz.sa.ws;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 处理 WebSocket 连接
 *
 * @author kong
 * @since: 2022-2-11
 */
public class MyWebSocketHandler extends TextWebSocketHandler {

    /** 固定前缀 */
    private static final String USER_ID = "user_id_";

    /** 存放Session集合，方便推送消息 */
    private static ConcurrentHashMap<String, WebSocketSession> webSocketSessionMaps = new ConcurrentHashMap<>();

    // 监听：连接开启
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // put到集合，方便后续操作
        String userId = session.getAttributes().get("userId").toString();
        webSocketSessionMaps.put(USER_ID + userId, session);

        // 给个提示
        String tips = "Web-Socket 连接成功，sid=" + session.getId() + "，userId=" + userId;
        LogUtils.info(tips);
        sendMessage(session, tips);
    }

    // 监听：连接关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 从集合移除
        String userId = session.getAttributes().get("userId").toString();
        webSocketSessionMaps.remove(USER_ID + userId);

        // 给个提示
        String tips = "Web-Socket 连接关闭，sid=" + session.getId() + "，userId=" + userId;
        LogUtils.info(tips);
    }

    // 收到消息
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        LogUtils.info("sid为：" + session.getId() + "，发来：" + message);
    }

    // -----------

    // 向指定客户端推送消息
    public static void sendMessage(WebSocketSession session, String message) {
        try {
            LogUtils.info("向sid为：" + session.getId() + "，发送：" + message);
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 向指定用户推送消息
    public static void sendMessage(long userId, String message) {
        WebSocketSession session = webSocketSessionMaps.get(USER_ID + userId);
        if (session != null) {
            sendMessage(session, message);
        }
    }
}
