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
import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrder;
import com.taotao.cloud.payment.biz.jeepay.core.entity.PayOrderDivisionRecord;
import com.taotao.cloud.payment.biz.jeepay.core.exception.BizException;
import com.taotao.cloud.payment.biz.jeepay.service.mapper.PayOrderDivisionRecordMapper;
import com.taotao.cloud.payment.biz.jeepay.service.mapper.PayOrderMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分账记录表 服务实现类
 *
 * @author [mybatis plus generator]
 * @since 2021-08-19
 */
@Service
public class PayOrderDivisionRecordService
        extends ServiceImpl<PayOrderDivisionRecordMapper, PayOrderDivisionRecord> {

    @Autowired private PayOrderMapper payOrderMapper;

    /** 更新分账记录为分账成功* */
    public void updateRecordSuccessOrFail(
            List<PayOrderDivisionRecord> records,
            Byte state,
            String channelBatchOrderId,
            String channelRespResult) {

        if (records == null || records.isEmpty()) {
            return;
        }

        List<Long> recordIds = new ArrayList<>();
        records.stream().forEach(r -> recordIds.add(r.getRecordId()));

        PayOrderDivisionRecord updateRecord = new PayOrderDivisionRecord();
        updateRecord.setState(state);
        updateRecord.setChannelBatchOrderId(channelBatchOrderId);
        updateRecord.setChannelRespResult(channelRespResult);
        update(
                updateRecord,
                PayOrderDivisionRecord.gw()
                        .in(PayOrderDivisionRecord::getRecordId, recordIds)
                        .eq(PayOrderDivisionRecord::getState, PayOrderDivisionRecord.STATE_WAIT));
    }

    /** 更新分账订单为： 等待分账中的状态 * */
    @Transactional
    public void updateResendState(String payOrderId) {

        PayOrder updateRecord = new PayOrder();
        updateRecord.setDivisionState(PayOrder.DIVISION_STATE_WAIT_TASK);

        // 更新订单
        int payOrderUpdateRow =
                payOrderMapper.update(
                        updateRecord,
                        PayOrder.gw()
                                .eq(PayOrder::getPayOrderId, payOrderId)
                                .eq(PayOrder::getDivisionState, PayOrder.DIVISION_STATE_FINISH));

        if (payOrderUpdateRow <= 0) {
            throw new BizException("更新订单分账状态失败");
        }

        PayOrderDivisionRecord updateRecordByDiv = new PayOrderDivisionRecord();
        updateRecordByDiv.setState(PayOrderDivisionRecord.STATE_WAIT); // 待分账
        updateRecordByDiv.setChannelRespResult("");
        updateRecordByDiv.setChannelBatchOrderId("");
        boolean recordUpdateFlag =
                update(
                        updateRecordByDiv,
                        PayOrderDivisionRecord.gw()
                                .eq(PayOrderDivisionRecord::getPayOrderId, payOrderId)
                                .eq(
                                        PayOrderDivisionRecord::getState,
                                        PayOrderDivisionRecord.STATE_FAIL));

        if (!recordUpdateFlag) {
            throw new BizException("更新分账记录状态失败");
        }
    }
}
