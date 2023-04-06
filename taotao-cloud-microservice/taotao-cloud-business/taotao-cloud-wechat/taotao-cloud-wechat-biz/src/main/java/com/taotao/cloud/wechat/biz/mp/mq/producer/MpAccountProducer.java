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

package com.taotao.cloud.wechat.biz.mp.mq.producer;

import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.mp.mq.message.MpAccountRefreshMessage;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 公众号账号 Producer
 *
 * @author 芋道源码
 */
@Component
public class MpAccountProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /** 发送 {@link MpAccountRefreshMessage} 消息 */
    public void sendAccountRefreshMessage() {
        MpAccountRefreshMessage message = new MpAccountRefreshMessage();
        redisMQTemplate.send(message);
    }
}
