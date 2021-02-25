package com.taotao.cloud.demo.rocketmq.transactional.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author zlt
 */
@EnableBinding({ Source.class, RocketMqConfig.MySink.class })
public class RocketMqConfig {

    public interface MySink {
        @Input("inputWithTx")
        SubscribableChannel input();

        @Input("inputDlq")
        SubscribableChannel inputDlq();
    }

}
