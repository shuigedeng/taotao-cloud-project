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

package com.taotao.cloud.mq.client.producer;

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.mq.client.producer.core.MqProducer;
import com.taotao.cloud.mq.client.producer.dto.SendBatchResult;
import com.taotao.cloud.mq.common.dto.req.MqMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class ProducerMainBatch {

    public static void main(String[] args) {
        MqProducer mqProducer = new MqProducer();
        mqProducer.start();

        List<MqMessage> mqMessageList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MqMessage mqMessage = buildMessage(i);
            mqMessageList.add(mqMessage);
        }

        SendBatchResult sendResult = mqProducer.sendBatch(mqMessageList);
        System.out.println(JSON.toJSON(sendResult));
    }

    private static MqMessage buildMessage(int i) {
        String message = "HELLO MQ!" + i;
        MqMessage mqMessage = new MqMessage();
        mqMessage.setTopic("TOPIC");
        mqMessage.setTags(Arrays.asList("TAGA", "TAGB"));
        mqMessage.setPayload(message);

        return mqMessage;
    }
}
