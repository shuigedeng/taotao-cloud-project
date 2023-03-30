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

package com.taotao.cloud.message.biz.austin.stream.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

/**
 * 消息队列工具类
 *
 * @author 3y
 */
@Slf4j
public class MessageQueueUtils {

    /**
     * 获取kafkaConsumer
     *
     * @param topicName
     * @param groupId
     * @return
     */
    public static KafkaSource<String> getKafkaConsumer(
            String topicName, String groupId, String broker) {
        KafkaSource<String> source =
                KafkaSource.<String>builder()
                        .setBootstrapServers(broker)
                        .setTopics(topicName)
                        .setGroupId(groupId)
                        .setStartingOffsets(OffsetsInitializer.earliest())
                        .setValueOnlyDeserializer(new SimpleStringSchema())
                        .build();
        return source;
    }
}
