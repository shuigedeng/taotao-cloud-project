package com.taotao.cloud.job.server.jobserver.consumer;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.remote.protos.MqCausa;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayedQueueManager {
    private static final Deque<MqCausa.Message> deadMessageQueue = new ArrayDeque<>();

    private static final List<DelayQueue<DelayedMessage>> delayQueueList = new ArrayList<>(2);
    /**
     * 逆序排序，因为重试次数到0则不再重试
     */
    private static List<Long> delayTimes = Lists.newArrayList(10000L, 5000L);
    public static void init(Consumer consumer) {
        delayQueueList.add(new DelayQueue<>());
        delayQueueList.add(new DelayQueue<>());
        Thread consumerThread1 = new Thread(() -> {
            try {
                while (true) {
                    // 从延时队列中取出消息（会等待直到消息到期）
                    DelayQueue<DelayedMessage> delayQueue = delayQueueList.get(0);
                    if(!delayQueue.isEmpty()) {
                        DelayedMessage message = delayQueue.take();
                        consumer.consume(message.message);
                        delayQueue.remove(message);
                        System.out.println("Consumed: " + message.getMessage() + " at " + System.currentTimeMillis());
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer thread interrupted");
            }
        });
        Thread consumerThread2 = new Thread(() -> {
            try {
                while (true) {

                    // 从延时队列中取出消息（会等待直到消息到期）
                    DelayQueue<DelayedMessage> delayQueue = delayQueueList.get(1);
                    if(!delayQueue.isEmpty()) {
                        DelayedMessage message = delayQueue.take();
                        consumer.consume(message.message);
                        delayQueue.remove(message);
                        System.out.println("Consumed: " + message.getMessage() + " at " + System.currentTimeMillis());
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer thread interrupted");
            }
        });
        consumerThread1.start();
        consumerThread2.start();
    }
    public static void reConsume(MqCausa.Message msg) {
        if (msg.getRetryTime() == 0) {
            log.error("msg : {} is dead", msg);
            deadMessageQueue.add(msg);
            return;
        }
        MqCausa.Message build = msg.toBuilder().setRetryTime(msg.getRetryTime() - 1).build();
        DelayedMessage delayedMessage = new DelayedMessage(build, delayTimes.get(build.getRetryTime()));
        delayQueueList.get(msg.getRetryTime() - 1).add(delayedMessage);
    }


// 定义一个延时消息类，实现 Delayed 接口
static class DelayedMessage implements Delayed {
    private final MqCausa.Message message;
    private final long triggerTime; // 到期时间

    public DelayedMessage(MqCausa.Message message, long delayTime) {
        this.message = message;
        // 当前时间加上延时时间，设置消息的触发时间
        this.triggerTime = System.currentTimeMillis() + delayTime;
    }

    // 获取剩余的延时时间
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(triggerTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    // 比较方法，用于确定消息的顺序
    @Override
    public int compareTo(Delayed other) {
        if (this.triggerTime < ((DelayedMessage) other).triggerTime) {
            return -1;
        } else if (this.triggerTime > ((DelayedMessage) other).triggerTime) {
            return 1;
        }
        return 0;
    }

    public MqCausa.Message getMessage() {
        return message;
    }
}
}
