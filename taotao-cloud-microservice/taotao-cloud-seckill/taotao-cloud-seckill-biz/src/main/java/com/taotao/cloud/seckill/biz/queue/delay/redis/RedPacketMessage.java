package com.taotao.cloud.seckill.biz.queue.delay.redis;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 红包队列消息
 */
public class RedPacketMessage implements Serializable {

    /**
     * 红包 ID
     */
    private  long redPacketId;

    /**
     * 创建时间戳
     */
    private  long timestamp;

    public RedPacketMessage() {

    }

    public RedPacketMessage(long redPacketId) {
        this.redPacketId = redPacketId;
        this.timestamp = System.currentTimeMillis();
    }

    public long getRedPacketId() {
        return redPacketId;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
