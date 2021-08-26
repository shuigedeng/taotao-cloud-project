package com.taotao.cloud.seckill.biz.queue.delay.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 红包过期失效 延迟队列
 */
public class RedPacketDelayQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketDelayQueue.class);

    public static void main(String[] args) throws Exception {
        DelayQueue<RedPacketMessage> queue = new DelayQueue<>();
        // 默认延迟3秒
        RedPacketMessage message = new RedPacketMessage(1);
        queue.add(message);
        // 延迟5秒
        message = new RedPacketMessage(2, 5);
        queue.add(message);
        // 延迟10秒
        message = new RedPacketMessage(3, 10);
        queue.add(message);
        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("DelayWorker");
            thread.setDaemon(true);
            return thread;
        });
        LOGGER.info("开始执行调度线程...");
        executorService.execute(() -> {
            while (true) {
                System.out.println("111");
                try {
                    RedPacketMessage task = queue.take();
                    System.out.println("111");
                    LOGGER.info("延迟处理红包消息,{}", task.getDescription());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
        Thread.sleep(Integer.MAX_VALUE);
    }
}
