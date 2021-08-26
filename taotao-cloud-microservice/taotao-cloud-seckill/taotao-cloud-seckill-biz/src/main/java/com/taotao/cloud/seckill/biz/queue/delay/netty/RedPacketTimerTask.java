package com.taotao.cloud.seckill.biz.queue.delay.netty;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 红包过期队列信息
 */
public class RedPacketTimerTask implements TimerTask {

    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 红包 ID
     */
    private final long redPacketId;

    /**
     * 创建时间戳
     */
    private final long timestamp;

    public RedPacketTimerTask(long redPacketId) {
        this.redPacketId = redPacketId;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void run(Timeout timeout) {
        System.out.println(String.format("任务执行时间:%s,红包创建时间:%s,红包ID:%s",
                LocalDateTime.now().format(F), LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()).format(F), redPacketId));
    }
}
