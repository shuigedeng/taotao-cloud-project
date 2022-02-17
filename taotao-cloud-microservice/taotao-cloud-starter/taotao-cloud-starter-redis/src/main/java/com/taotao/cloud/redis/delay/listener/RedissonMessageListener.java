package com.taotao.cloud.redis.delay.listener;



public interface RedissonMessageListener<T> {

    /**
     * on message method
     *
     * @param t message object
     * @throws Exception when consumer error
     */
    void onMessage(T t) throws Exception;

}
