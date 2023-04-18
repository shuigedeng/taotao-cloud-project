package com.taotao.cloud.bigdata.azkaban.mq.rabbitmq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * $RabbitMQProducerProvider 配置熟性信息
 *
 * @author clz.xu
 */
@ConfigurationProperties
@Data
public class RabbitMQProperties {

    public static String Project = "RabbitMQ";
    /**
     * rabbitmq 连接地址
     */
    @Value("${bsf.rabbitmq.restUri}")
    private String restUri;

    /**
     * 拉取消息数量
     */
    @Value("${bsf.rabbitmq.prefetchCount:3}")
    private Integer prefetchCount;

    /**
     * 并发消息者数
     */
    @Value("${bsf.rabbitmq.consumerCount:3}")
    private Integer consumerCount;

    /**
     * 最大并发消息者数
     */
    @Value("${bsf.rabbitmq.maxConsumerCount:10}")
    private Integer maxConsumerCount = 10;

    /**
     *  缓存数量默认1024
     */
    @Value("${bsf.rabbitmq.channelCacheSize:500}")
    private Integer channelCacheSize;


}
