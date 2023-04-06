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

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 事务提交后发生mq事件
 *
 * <pre>{@code
 * applicationEventPublisher.publishEvent(new TransactionCommitSendMQEvent("删除店铺商品", rocketmqCustomProperties.getGoodsTopic(), GoodsTagsEnum.STORE_GOODS_DELETE.name(), storeId));
 *
 * }</pre>
 */
public class TransactionCommitSendMQEvent extends ApplicationEvent {

    private static final long serialVersionUID = 5885956821347953071L;

    @Getter
    private final String topic;

    @Getter
    private final String tag;

    @Getter
    private final Object message;

    public TransactionCommitSendMQEvent(Object source, String topic, String tag, Object message) {
        super(source);
        this.topic = topic;
        this.tag = tag;
        this.message = message;
    }
}
