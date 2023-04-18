package com.taotao.cloud.bigdata.azkaban.mq.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Classname AbstractSubscribeRunable
 * @Description
 * @Date 2021/6/2 14:23
 * @Created by chejiangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AbstractSubscribeRunable<T> {
    String queueName;
    SubscribeRunable<T> runnable;
    Class<T> type;
}
