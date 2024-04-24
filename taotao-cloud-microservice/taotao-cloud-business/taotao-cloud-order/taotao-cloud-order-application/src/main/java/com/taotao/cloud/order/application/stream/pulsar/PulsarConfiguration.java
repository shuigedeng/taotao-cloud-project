/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.application.stream.pulsar;

import io.github.majusko.pulsar.consumer.ConsumerAggregator;
import io.github.majusko.pulsar.producer.ProducerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class PulsarConfiguration {

    @Bean
    public ProducerFactory producerFactory() {
        return new ProducerFactory()
                .addProducer(Product.PRODUCT_TOPIC, Product.class)
                .addProducer("other-topic", String.class);
    }

    @Configuration
    public static class PulsarErrorHandler {

        @Autowired
        private ConsumerAggregator aggregator;

        @EventListener(ApplicationReadyEvent.class)
        public void pulsarErrorHandler() {
            aggregator.onError(failedMessage -> failedMessage.getException().printStackTrace());
        }
    }
}
