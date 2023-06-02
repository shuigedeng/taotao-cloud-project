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

package com.taotao.cloud.message.biz.austin.handler.shield.impl;

import org.dromara.hutoolcore.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taotao.cloud.message.biz.austin.common.domain.AnchorInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.enums.AnchorState;
import com.taotao.cloud.message.biz.austin.common.enums.ShieldType;
import com.taotao.cloud.message.biz.austin.handler.shield.ShieldService;
import com.taotao.cloud.message.biz.austin.support.utils.RedisUtils;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 屏蔽服务
 *
 * @author 3y
 */
@Service
@Slf4j
public class ShieldServiceImpl implements ShieldService {

    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY = "night_shield_send";

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private LogUtils logUtils;

    @Override
    public void shield(TaskInfo taskInfo) {

        if (ShieldType.NIGHT_NO_SHIELD.getCode().equals(taskInfo.getShieldType())) {
            return;
        }

        /** example:当消息下发至austin平台时，已经是凌晨1点，业务希望此类消息在次日的早上9点推送 (配合 分布式任务定时任务框架搞掂) */
        if (isNight()) {
            if (ShieldType.NIGHT_SHIELD.getCode().equals(taskInfo.getShieldType())) {
                logUtils.print(AnchorInfo.builder()
                        .state(AnchorState.NIGHT_SHIELD.getCode())
                        .businessId(taskInfo.getBusinessId())
                        .ids(taskInfo.getReceiver())
                        .build());
            }
            if (ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(taskInfo.getShieldType())) {
                redisUtils.lPush(
                        NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY,
                        JSON.toJSONString(taskInfo, SerializerFeature.WriteClassName),
                        (DateUtil.offsetDay(new Date(), 1).getTime() / 1000) - DateUtil.currentSeconds());
                logUtils.print(AnchorInfo.builder()
                        .state(AnchorState.NIGHT_SHIELD_NEXT_SEND.getCode())
                        .businessId(taskInfo.getBusinessId())
                        .ids(taskInfo.getReceiver())
                        .build());
            }
            taskInfo.setReceiver(new HashSet<>());
        }
    }

    /**
     * 小时 < 8 默认就认为是凌晨(夜晚)
     *
     * @return
     */
    private boolean isNight() {
        return LocalDateTime.now().getHour() < 8;
    }
}
