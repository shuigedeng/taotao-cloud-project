package com.taotao.cloud.bigdata.azkaban.mq.base;

/**
 * @author: chejiangyi
 * @version: 2019-06-12 15:34
 * 订阅回调接口
 **/
public interface SubscribeRunable<T> {
     void run(Message<T> msg);
}
