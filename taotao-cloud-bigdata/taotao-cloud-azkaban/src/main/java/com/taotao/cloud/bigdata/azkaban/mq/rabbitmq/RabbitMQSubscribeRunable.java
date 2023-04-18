package com.taotao.cloud.bigdata.azkaban.mq.rabbitmq;

import com.free.bsf.mq.base.AbstractSubscribeRunable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname RabbitMQSubscribeRunable
 * @Description TODO
 * @Date 2021/6/2 14:49
 * @Created by chejiangyi
 */
@Data
@AllArgsConstructor
//@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class RabbitMQSubscribeRunable extends AbstractSubscribeRunable {
}
