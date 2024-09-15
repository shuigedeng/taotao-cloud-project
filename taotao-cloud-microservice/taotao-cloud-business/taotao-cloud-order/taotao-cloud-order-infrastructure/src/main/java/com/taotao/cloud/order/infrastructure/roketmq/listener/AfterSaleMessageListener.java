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

package com.taotao.cloud.order.infrastructure.roketmq.listener;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.stream.framework.rocketmq.tags.AfterSaleTagsEnum;
import com.taotao.cloud.order.infrastructure.model.entity.aftersale.AfterSale;
import com.taotao.cloud.order.infrastructure.roketmq.event.AfterSaleStatusChangeEvent;
import java.util.List;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 售后通知
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:34:09
 */
@Component
@RocketMQMessageListener(
        topic = "${taotao.data.rocketmq.after-sale-topic}",
        consumerGroup = "${taotao.data.rocketmq.after-sale-group}")
public class AfterSaleMessageListener implements RocketMQListener<MessageExt> {

    /** 售后订单状态 */
    @Autowired
    private List<AfterSaleStatusChangeEvent> afterSaleStatusChangeEvents;

    @Override
    public void onMessage(MessageExt messageExt) {
        if (AfterSaleTagsEnum.valueOf(messageExt.getTags()) == AfterSaleTagsEnum.AFTER_SALE_STATUS_CHANGE) {
            for (AfterSaleStatusChangeEvent afterSaleStatusChangeEvent : afterSaleStatusChangeEvents) {
                try {
                    AfterSale afterSale = JSONUtil.toBean(new String(messageExt.getBody()), AfterSale.class);
                    afterSaleStatusChangeEvent.afterSaleStatusChange(afterSale);
                } catch (Exception e) {
                    LogUtils.error(
                            "售后{},在{}业务中，状态修改事件执行异常",
                            new String(messageExt.getBody()),
                            afterSaleStatusChangeEvent.getClass().getName(),
                            e);
                }
            }
        }
    }
}
