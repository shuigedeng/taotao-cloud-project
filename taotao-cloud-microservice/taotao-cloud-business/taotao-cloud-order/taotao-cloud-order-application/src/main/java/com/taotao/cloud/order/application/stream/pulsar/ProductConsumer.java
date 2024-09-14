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

import com.taotao.boot.common.utils.log.LogUtils;
import io.github.majusko.pulsar.PulsarMessage;
import io.github.majusko.pulsar.annotation.PulsarConsumer;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {

    @PulsarConsumer(topic = Product.PRODUCT_TOPIC, clazz = Product.class)
    void consume(Product product) {
        // TODO process your message
        LogUtils.info(product.getData());
    }

    @PulsarConsumer(topic = Product.PRODUCT_TOPIC, clazz = Product.class)
    void consume(PulsarMessage<Product> message) {
        LogUtils.info(message.getValue().toString());
        // producer.send(TOPIC, msg.getValue());
    }
}
