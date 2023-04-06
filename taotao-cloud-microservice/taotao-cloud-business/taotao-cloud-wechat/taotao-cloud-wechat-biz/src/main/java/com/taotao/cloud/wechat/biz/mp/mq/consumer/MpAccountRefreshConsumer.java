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

package com.taotao.cloud.wechat.biz.mp.mq.consumer;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessageListener;
import cn.iocoder.yudao.module.mp.mq.message.MpAccountRefreshMessage;
import cn.iocoder.yudao.module.mp.service.account.MpAccountService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 针对 {@link MpAccountRefreshMessage} 的消费者
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class MpAccountRefreshConsumer extends AbstractChannelMessageListener<MpAccountRefreshMessage> {

    @Resource
    private MpAccountService mpAccountService;

    @Override
    public void onMessage(MpAccountRefreshMessage message) {
        log.info("[onMessage][收到 Account 刷新消息]");
        mpAccountService.initLocalCache();
    }
}
