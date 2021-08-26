package com.taotao.cloud.seckill.biz.queue.delay.redis;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

/**
 * 红包过期失效 高可用延迟队列
 * https://blog.52itstyle.vip/archives/5163/
 */
public class RedPacketDelayQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketDelayQueue.class);

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379")
                                .setPassword("123456").setDatabase(2);
        RedissonClient redissonClient = Redisson.create(config);

        /**
         * 红包目标队列
         */
        RBlockingQueue<RedPacketMessage> blockingRedPacketQueue
                = redissonClient.getBlockingQueue("redPacketDelayQueue");
        /**
         * 定时任务将到期的元素转移到目标队列
         */
        RDelayedQueue<RedPacketMessage> delayedRedPacketQueue
                = redissonClient.getDelayedQueue(blockingRedPacketQueue);

        /**
         * 延时信息入队列
         */
        delayedRedPacketQueue.offer(new RedPacketMessage(20200113), 3, TimeUnit.SECONDS);
        delayedRedPacketQueue.offer(new RedPacketMessage(20200114), 5, TimeUnit.SECONDS);
        delayedRedPacketQueue.offer(new RedPacketMessage(20200115), 10, TimeUnit.SECONDS);

        while (true){
            /**
             * 取出失效红包
             */
            RedPacketMessage redPacket = blockingRedPacketQueue.take();
            LOGGER.info("红包ID:{}过期失效",redPacket.getRedPacketId());
            /**
             * 处理相关业务逻辑：记录相关信息并退还剩余红包金额
             */
        }
    }
}


