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

package com.taotao.cloud.message.biz.austin.cron.handler;

import org.dromara.hutoolcore.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.support.config.SupportThreadPoolConfig;
import com.taotao.cloud.message.biz.austin.support.utils.RedisUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 夜间屏蔽的延迟处理类
 *
 * <p>example:当消息下发至austin平台时，已经是凌晨1点，业务希望此类消息在次日的早上9点推送
 *
 * @author 3y
 */
@Service
@Slf4j
public class NightShieldLazyPendingHandler {

    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY = "night_shield_send";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${austin.business.topic.name}")
    private String topicName;

    @Autowired
    private RedisUtils redisUtils;

    /** 处理 夜间屏蔽(次日早上9点发送的任务) */
    @XxlJob("nightShieldLazyJob")
    public void execute() {
        log.info("NightShieldLazyPendingHandler#execute!");
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (redisUtils.lLen(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY) > 0) {
                String taskInfo = redisUtils.lPop(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY);
                if (StrUtil.isNotBlank(taskInfo)) {
                    try {
                        kafkaTemplate.send(
                                topicName,
                                JSON.toJSONString(
                                        Arrays.asList(JSON.parseObject(taskInfo, TaskInfo.class)),
                                        new SerializerFeature[] {SerializerFeature.WriteClassName}));
                    } catch (Exception e) {
                        log.error(
                                "nightShieldLazyJob send kafka fail!" + " e:{},params:{}",
                                Throwables.getStackTraceAsString(e),
                                taskInfo);
                    }
                }
            }
        });
    }
}
