package com.taotao.cloud.bigdata.azkaban.mq.rabbitmq;

import com.free.bsf.core.common.Collector;

/**
 * $RabbitMQMonitor rabbitmq 信息收集器
 *
 * @author clz.xu
 */
public class RabbitMQMonitor {

    private static String name="rabbitmq.info";

    public static Collector.Hook hook(){
        return Collector.Default.hook(name+".hook");
    }
}
