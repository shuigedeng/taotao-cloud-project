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

package com.taotao.cloud.order.application.task.disruptor;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.eventbus.disruptor.tmp3.annotation.EventRule;
import com.taotao.boot.eventbus.disruptor.tmp3.event.DisruptorBindEvent;
import com.taotao.boot.eventbus.disruptor.tmp3.handler.DisruptorHandler;
import com.taotao.boot.eventbus.disruptor.tmp3.handler.HandlerChain;
import org.springframework.stereotype.Component;

/**
 * EmailDisruptorHandler
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @since 2021/09/03 18:02
 */
@EventRule("/Event-Output/TagA-Output/**")
@Component("emailHandler")
public class EmailDisruptorHandler implements DisruptorHandler<DisruptorBindEvent> {

    @Override
    public void doHandler(DisruptorBindEvent event, HandlerChain<DisruptorBindEvent> handlerChain) throws Exception {
        LogUtils.info(event.toString());
    }
}
