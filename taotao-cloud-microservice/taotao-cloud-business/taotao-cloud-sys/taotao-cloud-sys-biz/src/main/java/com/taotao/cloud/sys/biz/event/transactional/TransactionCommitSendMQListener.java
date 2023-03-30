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

package com.taotao.cloud.sys.biz.event.transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** 事务提交监听器 */
@Component
@Slf4j
public class TransactionCommitSendMQListener {

    /** rocketMq */
    // @Autowired
    // private RocketMQTemplate rocketMQTemplate;
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(TransactionCommitSendMQEvent event) {
        log.info("事务提交，发送mq信息!{}", event);
        String destination = event.getTopic() + ":" + event.getTag();
        // 发送订单变更mq消息
        // rocketMQTemplate.asyncSend(destination, event.getMessage(),
        //	RocketmqSendCallbackBuilder.commonCallback());
    }
}
