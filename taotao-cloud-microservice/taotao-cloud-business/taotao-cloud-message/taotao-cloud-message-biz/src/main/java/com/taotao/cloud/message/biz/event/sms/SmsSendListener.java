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

package com.taotao.cloud.message.biz.event.sms;

import com.taotao.boot.sms.common.event.SmsSendFailEvent;
import com.taotao.boot.sms.common.event.SmsSendFinallyEvent;
import com.taotao.boot.sms.common.event.SmsSendSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * SmsSendListener
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Component
public class SmsSendListener {

    @Async
    @EventListener(SmsSendFailEvent.class)
    public void smsSendFailEventListener( SmsSendFailEvent event ) {
    }

    @Async
    @EventListener(SmsSendSuccessEvent.class)
    public void smsSendSuccessEventListener( SmsSendSuccessEvent event ) {
    }

    @Async
    @EventListener(SmsSendFinallyEvent.class)
    public void smsSendFinallyEventListener( SmsSendFinallyEvent event ) {
    }
}
