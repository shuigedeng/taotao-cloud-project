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

package com.taotao.cloud.payment.biz.kit.params;

import com.taotao.cloud.payment.api.enums.CashierEnum;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;

/**
 * 收银台接口
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-02 14:43:36
 */
public interface CashierExecute {

    /**
     * 获取支付参数
     *
     * @param payParam 收银台支付参数
     * @return {@link CashierParam }
     * @since 2022-06-02 14:43:36
     */
    CashierParam getPaymentParams(PayParam payParam);

    /**
     * 支付成功
     *
     * @param paymentSuccessParams 支付回调
     * @since 2022-06-02 14:43:36
     */
    void paymentSuccess(PaymentSuccessParams paymentSuccessParams);

    /**
     * 支付结果查询
     *
     * @param payParam
     * @return {@link Boolean }
     * @since 2022-06-02 14:43:36
     */
    Boolean paymentResult(PayParam payParam);

    /**
     * 服务的枚举类型
     *
     * @return {@link CashierEnum }
     * @since 2022-06-02 14:43:36
     */
    CashierEnum cashierEnum();
}
