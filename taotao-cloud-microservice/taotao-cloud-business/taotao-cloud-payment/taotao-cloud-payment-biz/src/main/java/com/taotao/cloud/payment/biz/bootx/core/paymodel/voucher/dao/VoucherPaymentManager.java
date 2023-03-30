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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao;

import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.payment.core.paymodel.voucher.entity.VoucherPayment;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xxm
 * @date 2022/3/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherPaymentManager extends BaseManager<VoucherPaymentMapper, VoucherPayment> {

    /** 根据支付id */
    public Optional<VoucherPayment> findByPaymentId(Long paymentId) {
        return this.findByField(VoucherPayment::getPaymentId, paymentId);
    }
}
