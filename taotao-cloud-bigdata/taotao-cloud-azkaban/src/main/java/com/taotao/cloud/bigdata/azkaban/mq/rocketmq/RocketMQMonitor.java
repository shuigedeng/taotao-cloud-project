package com.taotao.cloud.bigdata.azkaban.mq.rocketmq;

import com.free.bsf.core.common.Collector;

/**
 * @author Huang Zhaoping
 */
public class RocketMQMonitor {
    private static String name="rocketmq.info";

    public static Collector.Hook hook(){
        return Collector.Default.hook(name+".hook");
    }
}
