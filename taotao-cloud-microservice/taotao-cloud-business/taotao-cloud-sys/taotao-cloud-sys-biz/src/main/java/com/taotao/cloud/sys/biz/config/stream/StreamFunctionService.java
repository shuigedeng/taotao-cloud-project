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

package com.taotao.cloud.sys.biz.config.stream;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 流函数服务
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:07
 */
@Component
public class StreamFunctionService {

    @Autowired
    private StreamBridge bridge;

    public void sendRocketmqExample() {
        boolean s1 = bridge.send("example-out-0", "topic example");
        LogUtils.info("example send msg:{}", s1);
    }

    public void sendRocketmqDemo() throws Exception {
        // tag 发送
        String payload = "消息体demo1发送到tag s1";
        Map<String, Object> headers = new HashMap<>();
        headers.put(MessageConst.PROPERTY_TAGS, "s1");
        MessageHeaders messageHeaders = new MessageHeaders(headers);
        Message<String> message = MessageBuilder.withPayload(payload)
                .copyHeadersIfAbsent(messageHeaders)
                .build();
        boolean s3 = bridge.send("demo1-out-0", message);
        LogUtils.info("demo1 send msg:{}", s3);
    }

    public void sendRocketmqTest() throws Exception {
        String payload = "消息体tag s3 延迟消息 topic test";
        Map<String, Object> headers = new HashMap<>();
        // 延迟消费 延迟10秒
        headers.put(MessageConst.PROPERTY_DELAY_TIME_LEVEL, "3");
        MessageHeaders messageHeaders = new MessageHeaders(headers);
        Message<String> message = MessageBuilder.withPayload(payload)
                .copyHeadersIfAbsent(messageHeaders)
                .build();
        boolean s3 = bridge.send("test1-out-0", message);
        LogUtils.info("test send msg:{}", s3);
    }

    public void sendKafka(String content) {
        boolean send = bridge.send("outputKafka-out-0", content);
        LogUtils.info(String.valueOf(send));
    }

    public void sendRabbit(String content) {
        boolean send = bridge.send("outputRabbit-out-0", content);
        LogUtils.info(String.valueOf(send));
    }

    public void sendRocketmq(String content) {
        boolean send = bridge.send("outputEmail-out-0", content);
        LogUtils.info(String.valueOf(send));
    }

    @Bean
    public Consumer<String> inputKafka1() {
        return str -> {
            // 收到消息在这里做一些处理
            LogUtils.info("inputKafka1 message: {}", str);
        };
    }

    @Bean
    public Consumer<String> inputKafka2() {
        return str -> {
            LogUtils.info("inputKafka2 message: {}", str);
        };
    }

    @Bean
    public Consumer<String> inputRabbit1() {
        return str -> {
            LogUtils.info("inputRabbit1 message: {}", str);
        };
    }

    @Bean
    public Consumer<String> inputRabbit2() {
        return str -> {
            LogUtils.info("inputRabbit2 message: {}", str);
        };
    }

    @Bean
    public Consumer<Message<String>> demo() {
        return message -> {
            LogUtils.info("demo1获取消息tag:{}", message.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TAGS));
            LogUtils.info("demo1接收数据:{}", message.getPayload());
        };
    }

    @Bean
    public Function<Flux<Message<String>>, Mono<Void>> example() {
        return flux -> flux.map(message -> {
                    LogUtils.info("example接收数据:{}", message.getPayload());
                    return message;
                })
                .then();
    }

    @Bean
    public Consumer<Flux<Message<String>>> test() {
        return flux -> flux.map(message -> {
                    LogUtils.info("test接收数据:{}", message.getPayload());
                    return message;
                })
                .subscribe();
    }

    // @Bean
    // public Function<KStream<Long, Order>,
    //	Function<GlobalKTable<Long, Customer>,
    //		Function<GlobalKTable<Long, Product>, KStream<Long, EnrichedOrder>>>> enrichOrder() {
    //
    //	return orders -> (
    //		customers -> (
    //			products -> (
    //				orders.join(customers,
    //						(orderId, order) -> order.getCustomerId(),
    //						(order, customer) -> new CustomerOrder(customer, order))
    //					.join(products,
    //						(orderId, customerOrder) -> customerOrder
    //							.productId(),
    //						(customerOrder, product) -> {
    //							EnrichedOrder enrichedOrder = new EnrichedOrder();
    //							enrichedOrder.setProduct(product);
    //							enrichedOrder.setCustomer(customerOrder.customer);
    //							enrichedOrder.setOrder(customerOrder.order);
    //							return enrichedOrder;
    //						})
    //			)
    //		)
    //	);
    // }

    // @Bean
    // public Function<KStream<Object, String>, KStream<?, WordCount>[]> process() {
    //
    //	Predicate<Object, WordCount> isEnglish = (k, v) -> v.word.equals("english");
    //	Predicate<Object, WordCount> isFrench = (k, v) -> v.word.equals("french");
    //	Predicate<Object, WordCount> isSpanish = (k, v) -> v.word.equals("spanish");
    //
    //	return input -> input
    //		.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
    //		.groupBy((key, value) -> value)
    //		.windowedBy(TimeWindows.of(5000))
    //		.count(Materialized.as("WordCounts-branch"))
    //		.toStream()
    //		.map((key, value) -> new KeyValue<>(null, new WordCount(key.key(), value,
    //			new Date(key.window().start()), new Date(key.window().end()))))
    //		.branch(isEnglish, isFrench, isSpanish);
    // }

}
