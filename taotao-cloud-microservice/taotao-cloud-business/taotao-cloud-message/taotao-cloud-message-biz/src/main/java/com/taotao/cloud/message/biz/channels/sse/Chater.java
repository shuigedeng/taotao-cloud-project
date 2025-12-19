package com.taotao.cloud.message.biz.channels.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Chater
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class Chater {

    private String userName;
    private SseEmitter sseEmitter;
    private Queue<MessageDTO<?>> msgList = new ConcurrentLinkedQueue<>();

    public void addMsg( MessageDTO<?> msg ) {
        msgList.add(msg);
        while (!msgList.isEmpty()) {
            MessageDTO<?> msgItem = msgList.poll();
            try {
                sseEmitter.send(msgItem);
            } catch (IOException e) {
                LogUtils.error(e);
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public SseEmitter getSseEmitter() {
        return sseEmitter;
    }

    public void setSseEmitter( SseEmitter sseEmitter ) {
        this.sseEmitter = sseEmitter;
    }

}
