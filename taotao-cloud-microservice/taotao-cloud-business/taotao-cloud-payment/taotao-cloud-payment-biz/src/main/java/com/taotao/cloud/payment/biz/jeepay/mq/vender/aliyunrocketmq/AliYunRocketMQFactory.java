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

package com.taotao.cloud.payment.biz.jeepay.mq.vender.aliyunrocketmq;

import com.aliyun.openservices.ons.api.*;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = MQVenderCS.YML_VENDER_KEY, havingValue = MQVenderCS.ALIYUN_ROCKET_MQ)
public class AliYunRocketMQFactory {

    public static final String defaultTag = "Default";

    @Value("${aliyun-rocketmq.namesrvAddr:}")
    public String namesrvAddr;

    @Value("${aliyun-rocketmq.accessKey}")
    private String accessKey;

    @Value("${aliyun-rocketmq.secretKey}")
    private String secretKey;

    @Value("${aliyun-rocketmq.groupId}")
    private String groupId;

    @Bean(name = "producerClient")
    public Producer producerClient() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.GROUP_ID, groupId);
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        // 判断是否为空（生产环境走k8s集群环境变量自动注入，不获取本地配置文件的值）
        if (StringUtils.isNotEmpty(namesrvAddr)) {
            properties.put(PropertyKeyConst.NAMESRV_ADDR, namesrvAddr);
        }
        return ONSFactory.createProducer(properties);
    }

    @Bean(name = "consumerClient")
    public Consumer consumerClient() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.GROUP_ID, groupId);
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        // 判断是否为空（生产环境走k8s集群环境变量自动注入，不获取本地配置文件的值）
        if (StringUtils.isNotEmpty(namesrvAddr)) {
            properties.put(PropertyKeyConst.NAMESRV_ADDR, namesrvAddr);
        }
        return ONSFactory.createConsumer(properties);
    }

    @Bean(name = "broadcastConsumerClient")
    public Consumer broadcastConsumerClient() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.GROUP_ID, groupId);
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        // 广播订阅方式设置
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        // 判断是否为空（生产环境走k8s集群环境变量自动注入，不获取本地配置文件的值）
        if (StringUtils.isNotEmpty(namesrvAddr)) {
            properties.put(PropertyKeyConst.NAMESRV_ADDR, namesrvAddr);
        }
        return ONSFactory.createConsumer(properties);
    }
}
