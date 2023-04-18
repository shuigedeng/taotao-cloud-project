package com.taotao.cloud.bigdata.azkaban.mq.rabbitmq;

import com.free.bsf.core.util.LogUtils;
import lombok.Getter;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.StringUtils;


/**
 * $RabbitMQConfiguration  配置信息
 *
 * @author clz.xu
 */
@Configuration
@EnableConfigurationProperties(RabbitMQProperties.class)
@ConditionalOnProperty( name = {"bsf.mq.enabled", "bsf.rabbitmq.enabled"} , havingValue = "true")
@Getter
@EnableRabbit
public class RabbitMQConfiguration implements InitializingBean {

    @Autowired
    RabbitMQProperties rabbitMQProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(rabbitMQProperties.getRestUri());
        factory.setChannelCacheSize(rabbitMQProperties.getChannelCacheSize());
        factory.setPublisherConfirms(true);
        factory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        return factory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(rabbitMQProperties.getConsumerCount());
        factory.setMaxConcurrentConsumers(rabbitMQProperties.getMaxConsumerCount());
        factory.setPrefetchCount(rabbitMQProperties.getPrefetchCount());
        factory.setRetryTemplate(new RetryTemplate());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }


    @Bean(destroyMethod = "close")
    @Lazy
    public RabbitMQProducerProvider getRabbitMQProducerProvider(ConnectionFactory connectionFactory) {
        if (!StringUtils.isEmpty(rabbitMQProperties.getRestUri())) {
            return new RabbitMQProducerProvider(connectionFactory);
        }
        return null;
    }

    @Bean(destroyMethod = "close")
    @Lazy
    public RabbitMQConsumerProvider getRabbitMQConsumerProvider(ConnectionFactory connectionFactory) {
        if (!StringUtils.isEmpty(rabbitMQProperties.getRestUri())) {
            return new RabbitMQConsumerProvider(connectionFactory);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LogUtils.info(RabbitMQConfiguration.class, RabbitMQProperties.Project, "已启动!!!");
    }
}
