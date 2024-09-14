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

package com.taotao.cloud.order.application.stream.consumer;

import com.taotao.boot.common.utils.log.LogUtils;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;

/** 消息订单消息 */
@Service
public class OrderConsumerService {

    /** 消费分布式事务消息 */
    @Bean
    public Consumer<String> order() {
        return order -> {
            LogUtils.info("接收的普通消息为：{}", order);
        };
    }

    /**
     * 自定义全局异常处理
     *
     * @param message 消息体
     */
    public void error(Message<?> message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        LogUtils.error("Handling ERROR, errorMessage = {} ", errorMessage);
    }
}
