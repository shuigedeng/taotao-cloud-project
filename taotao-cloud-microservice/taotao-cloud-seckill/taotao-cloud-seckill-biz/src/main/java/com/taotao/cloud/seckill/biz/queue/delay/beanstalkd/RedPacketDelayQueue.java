package com.taotao.cloud.seckill.biz.queue.delay.beanstalkd;

import com.dinstone.beanstalkc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 高性能，轻量级的分布式内存队列
 * 特性:
 * 1、支持优先级(支持任务插队)
 * 2、延迟(实现定时任务)
 * 3、持久化(定时把内存中的数据刷到binlog日志)
 * 4、预留(把任务设置成预留，消费者无法取出任务，等某个合适时机再拿出来处理)
 * 5、任务超时重发(消费者必须在指定时间内处理任务，如果没有则认为任务失败，重新进入队列)
 * https://beanstalkd.github.io/
 * https://www.cnblogs.com/xiexj/p/7644999.html
 * https://blog.csdn.net/jiaobuchong/article/details/81040829
 */
public class RedPacketDelayQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketDelayQueue.class);

    public static void main(String[] args) {
        /**
         * 初始化基础配置
         */
        Configuration config = new Configuration();
        config.setServiceHost("58.87.107.182");
        config.setServicePort(11300);
        BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
        JobProducer producer = factory.createJobProducer("redPacketDelayQueue");
        String msg = "你好, 微信红包";
        /**
         * priority：优先级
         * delay：延迟多长时间开始执行，单位秒
         * ttr: 单位秒，为 consumer 操作设置的 reserve超时时间,
         * 如果 consumer 在这个ttr时间里没有完成 job 并将 job delete掉，
         * 那这个job就会重新被迁回ready状态，再次供消费者执行
         */
        producer.putJob(100, 1, 5, msg.getBytes());
        JobConsumer consumer = factory.createJobConsumer("redPacketDelayQueue");
        while (true) {
            /**
             * 超时时间参数，单位是秒，表示获取消息最多花费多长时间
             */
            Job job = consumer.reserveJob(3);
            if (Objects.isNull(job)) {
                continue;
            }
            /**
             * 消费任务
             */
            consumer.deleteJob(job.getId());
            /**
             * 处理业务逻辑
             */
            LOGGER.info("任务ID：{}",job.getId());
            LOGGER.info("任务信息：{}",new String(job.getData()));
        }
    }
}


