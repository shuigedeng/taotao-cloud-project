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

package com.taotao.cloud.payment.biz.bootx.core.notify.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.notify.entity.PayNotifyRecord;
import com.taotao.cloud.payment.biz.bootx.dto.notify.PayNotifyRecordDto;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 支付消息通知回调
 *
 * @author xxm
 * @date 2021/6/22
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayNotifyRecordManager extends BaseManager<PayNotifyRecordMapper, PayNotifyRecord> {

    public Page<PayNotifyRecord> page(PageQuery PageQuery, PayNotifyRecordDto param) {
        Page<PayNotifyRecord> mpPage = MpUtil.getMpPage(PageQuery, PayNotifyRecord.class);
        return lambdaQuery()
                .orderByDesc(MpBaseEntity::getId)
                .like(
                        Objects.nonNull(param.getPaymentId()),
                        PayNotifyRecord::getPaymentId,
                        param.getPaymentId())
                .eq(
                        Objects.nonNull(param.getPayChannel()),
                        PayNotifyRecord::getPayChannel,
                        param.getPayChannel())
                .eq(
                        Objects.nonNull(param.getStatus()),
                        PayNotifyRecord::getStatus,
                        param.getStatus())
                .page(mpPage);
    }
}
