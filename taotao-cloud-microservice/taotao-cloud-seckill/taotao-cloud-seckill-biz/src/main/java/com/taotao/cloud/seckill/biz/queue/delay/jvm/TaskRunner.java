package com.taotao.cloud.seckill.biz.queue.delay.jvm;

import com.taotao.cloud.seckill.biz.common.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 红包延迟队列过期消费
 */
@Component("redPacket")
public class TaskRunner implements ApplicationRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

    @Autowired
    private RedisUtil redisUtil;

    ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("RedPacketDelayWorker");
        thread.setDaemon(true);
        return thread;
    });

    @Override
    public void run(ApplicationArguments var){
        executorService.execute(() -> {
            while (true) {
                try {
                    RedPacketMessage message = RedPacketQueue.getQueue().consume();
                    if(message!=null){
                        long redPacketId = message.getRedPacketId();
                        LOGGER.info("红包{}过期了",redPacketId);
                        /**
                         * 获取剩余红包个数以及金额
                         */
                        int num = (int) redisUtil.getValue(redPacketId+"-num");
                        int restMoney = (int) redisUtil.getValue(redPacketId+"-money");
                        LOGGER.info("剩余红包个数{}，剩余红包金额{}",num,restMoney);
                        /**
                         * 清空红包数据
                         */
                        redisUtil.removeValue(redPacketId+"-num");
                        redisUtil.removeValue(redPacketId+"-money");
                        /**
                         * 异步更新数据库、异步退回红包金额
                         */
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
