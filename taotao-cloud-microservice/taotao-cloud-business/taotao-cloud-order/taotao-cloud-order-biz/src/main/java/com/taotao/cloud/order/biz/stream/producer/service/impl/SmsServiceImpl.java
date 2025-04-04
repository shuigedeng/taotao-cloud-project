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

package com.taotao.cloud.order.biz.stream.producer.service.impl;

import com.taotao.cloud.mq.pulsar.constant.MessageConstant;
import com.taotao.cloud.order.biz.stream.producer.service.ISmsService;
import lombok.*;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

/** 发送短信实现类 */
@Service
@AllArgsConstructor
public class SmsServiceImpl implements ISmsService {

    private final StreamBridge streamBridge;

    /**
     * 采用StreamBridge的发送方式
     *
     * @param message 　短消息
     * @link
     *     https://docs.spring.io/spring-cloud-stream/docs/3.1.0/reference/html/spring-cloud-stream.html#_binding_and_binding_names
     */
    @Override
    public void sendSms(String message) {
        streamBridge.send(MessageConstant.SMS_MESSAGE_OUTPUT, message);
    }
}
