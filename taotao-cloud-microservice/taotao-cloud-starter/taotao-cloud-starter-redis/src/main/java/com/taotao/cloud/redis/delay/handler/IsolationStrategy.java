package com.taotao.cloud.redis.delay.handler;


/**
 * IsolationStrategy 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public interface IsolationStrategy {

    /**
     * acquire queue name apply to redis
     *
     * @param queue queue name
     * @return actual queue name at redis server
     */
    String getRedisQueueName(String queue);

}
