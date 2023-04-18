package com.taotao.cloud.bigdata.azkaban.mq.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Classname AbstractMQMessage
 * @Description TODO
 * @Date 2021/6/2 14:00
 * @Created by chejiangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AbstractSendMessage<T> {
    /**
     * 队列名
     */
    String queueName;
    /**
     * 消息体
     */
    T msg;

}
