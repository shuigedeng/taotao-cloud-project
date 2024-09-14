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

package com.taotao.cloud.goods.application.config.redisson;

import com.taotao.boot.cache.redis.delay.MessageConversionException;
import com.taotao.boot.cache.redis.delay.annotation.RedissonListener;
import com.taotao.boot.cache.redis.delay.config.RedissonQueue;
import com.taotao.boot.cache.redis.delay.message.DefaultRedissonMessageConverter;
import com.taotao.boot.cache.redis.delay.message.MessageConverter;
import com.taotao.boot.cache.redis.delay.message.QueueMessage;
import com.taotao.boot.cache.redis.delay.message.QueueMessageBuilder;
import com.taotao.boot.cache.redis.delay.message.RedissonHeaders;
import com.taotao.boot.cache.redis.delay.message.RedissonMessage;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.Map;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * redisson延迟应用程序
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:53:44
 */
@Configuration
public class RedissonDelayApplication {

    @Bean
    public RedissonQueue redissonQueue() {
        return new RedissonQueue("riven", true, null, new DefaultRedissonMessageConverter());
    }

    @Bean("myMessageConverter")
    public MessageConverter messageConverter() {
        return new MessageConverter() {
            @Override
            public QueueMessage<?> toMessage(Object object, Map<String, Object> headers)
                    throws MessageConversionException {
                // do something you want, eg:
                headers.put("my_header", "my_header_value");
                return QueueMessageBuilder.withPayload(object).headers(headers).build();
            }

            @Override
            public Object fromMessage(RedissonMessage redissonMessage) throws MessageConversionException {
                String payload = redissonMessage.getPayload();
                String payloadStr = new String(payload);
                return JsonUtils.toObject(payloadStr, CarLbsDto.class);
            }
        };
    }

    @RedissonListener(queues = "riven", messageConverter = "myMessageConverter")
    public void handler(
            @Header(value = RedissonHeaders.MESSAGE_ID, required = false) String messageId,
            @Header(RedissonHeaders.DELIVERY_QUEUE_NAME) String queue,
            @Header(RedissonHeaders.SEND_TIMESTAMP) long sendTimestamp,
            @Header(RedissonHeaders.EXPECTED_DELAY_MILLIS) long expectedDelayMillis,
            @Header(value = "my_header", required = false, defaultValue = "test") String myHeader,
            @Payload CarLbsDto carLbsDto) {
        LogUtils.info(messageId);
        LogUtils.info(queue);
        LogUtils.info(myHeader);
        long actualDelay = System.currentTimeMillis() - (sendTimestamp + expectedDelayMillis);
        LogUtils.info("receive " + carLbsDto + ", delayed " + actualDelay + " millis");
    }

    @Data
    public static class CarLbsDto {

        private String cid;
        private String businessType;
        private String city;
        private String cityId;
        private String name;
        private String carNum;
    }
}
