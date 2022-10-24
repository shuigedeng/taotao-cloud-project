package com.taotao.cloud.cache.redis.delay.listener;


/**
 * RedissonMessageListener 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:42
 */
public interface RedissonMessageListener<T> {

    /**
     * on message method
     *
     * @param t message object
     * @throws Exception when consumer error
     */
    void onMessage(T t) throws Exception;

}
