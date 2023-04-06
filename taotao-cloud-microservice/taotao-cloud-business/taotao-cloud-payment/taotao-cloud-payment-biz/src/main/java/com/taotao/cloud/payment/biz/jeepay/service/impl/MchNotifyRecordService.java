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

package com.taotao.cloud.payment.biz.jeepay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.payment.biz.jeepay.core.entity.MchNotifyRecord;
import com.taotao.cloud.payment.biz.jeepay.service.mapper.MchNotifyRecordMapper;
import org.springframework.stereotype.Service;

/**
 * 商户通知表 服务实现类
 *
 * @author [mybatis plus generator]
 * @since 2021-04-27
 */
@Service
public class MchNotifyRecordService extends ServiceImpl<MchNotifyRecordMapper, MchNotifyRecord> {

    /** 根据订单号和类型查询 */
    public MchNotifyRecord findByOrderAndType(String orderId, Byte orderType) {
        return getOne(MchNotifyRecord.gw()
                .eq(MchNotifyRecord::getOrderId, orderId)
                .eq(MchNotifyRecord::getOrderType, orderType));
    }

    /** 查询支付订单 */
    public MchNotifyRecord findByPayOrder(String orderId) {
        return findByOrderAndType(orderId, MchNotifyRecord.TYPE_PAY_ORDER);
    }

    /** 查询退款订单订单 */
    public MchNotifyRecord findByRefundOrder(String orderId) {
        return findByOrderAndType(orderId, MchNotifyRecord.TYPE_REFUND_ORDER);
    }

    /** 查询退款订单订单 */
    public MchNotifyRecord findByTransferOrder(String transferId) {
        return findByOrderAndType(transferId, MchNotifyRecord.TYPE_TRANSFER_ORDER);
    }

    public Integer updateNotifyResult(Long notifyId, Byte state, String resResult) {
        return baseMapper.updateNotifyResult(notifyId, state, resResult);
    }
}
