package com.taotao.cloud.bigdata.azkaban.mq;

import com.free.bsf.core.config.BsfConfiguration;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.mq.rabbitmq.RabbitMQConfiguration;
import com.free.bsf.mq.rocketmq.RocketMQConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * @author: chejiangyi
 * @version: 2019-06-12 12:02
 **/
@Configuration
@ConditionalOnProperty(name = "bsf.mq.enabled", havingValue = "true")
@EnableConfigurationProperties(MQProperties.class)
@Import({BsfConfiguration.class,RocketMQConfiguration.class, RabbitMQConfiguration.class})
public class MQConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        LogUtils.info(MQConfiguration.class,MQProperties.Project,"已启动!!!");
    }

}
