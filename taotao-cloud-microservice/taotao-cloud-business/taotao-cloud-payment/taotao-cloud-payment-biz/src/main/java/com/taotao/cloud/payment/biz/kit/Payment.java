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

package com.taotao.cloud.payment.biz.kit;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 支付接口
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-02 14:44:06
 */
public interface Payment {

    /**
     * 普通移动网页调用支付app
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param payParam api参数
     * @return {@link Result }<{@link Object }>
     * @since 2022-06-02 14:44:06
     */
    default Result<Object> h5pay(HttpServletRequest request, HttpServletResponse response, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 公众号内部调用支付
     *
     * @param request HttpServletRequest
     * @param payParam api参数
     * @return {@link Result }<{@link Object }>
     * @since 2022-06-02 14:44:06
     */
    default Result<Object> jsApiPay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * app支付
     *
     * @param request HttpServletRequest
     * @param payParam 支付参数
     * @return {@link Result }<{@link Object }>
     * @since 2022-06-02 14:44:06
     */
    default Result<Object> appPay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 展示二维码扫描支付
     *
     * @param request HttpServletRequest
     * @param payParam 支付参数
     * @return {@link Result }<{@link Object }>
     * @since 2022-06-02 14:44:06
     */
    default Result<Object> nativePay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 小程序支付
     *
     * @param request HttpServletRequest
     * @param payParam 支付参数
     * @return {@link Result }<{@link Object }>
     * @since 2022-06-02 14:44:06
     */
    default Result<Object> mpPay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 退款
     *
     * @param refundLog 退款请求参数
     * @since 2022-06-02 14:44:06
     */
    default void refund(RefundLog refundLog) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 取消支付订单
     *
     * @param refundLog 支付参数
     * @since 2022-06-02 14:44:06
     */
    default void cancel(RefundLog refundLog) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 回调
     *
     * @param request HttpServletRequest
     * @since 2022-06-02 14:44:06
     */
    default void callBack(HttpServletRequest request) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 异步通知
     *
     * @param request HttpServletRequest
     * @since 2022-06-02 14:44:06
     */
    default void notify(HttpServletRequest request) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 退款异步通知
     *
     * @param request HttpServletRequest
     * @since 2022-06-02 14:44:06
     */
    default void refundNotify(HttpServletRequest request) {
        throw new BusinessException(ResultEnum.PAY_ERROR);
    }

    /**
     * 支付回调地址
     *
     * @param api api地址
     * @param paymentMethodEnum 支付类型
     * @return {@link String }
     * @since 2022-06-02 14:44:06
     */
    default String callbackUrl(String api, PaymentMethodEnum paymentMethodEnum) {
        return api + "/buyer/payment/cashier/callback/" + paymentMethodEnum.name();
    }

    /**
     * 支付异步通知地址
     *
     * @param api api地址
     * @param paymentMethodEnum 支付类型
     * @return {@link String }
     * @since 2022-06-02 14:44:06
     */
    default String notifyUrl(String api, PaymentMethodEnum paymentMethodEnum) {
        return api + "/buyer/payment/cashier/notify/" + paymentMethodEnum.name();
    }

    /**
     * 退款支付异步通知地址
     *
     * @param api api地址
     * @param paymentMethodEnum 支付类型
     * @return {@link String }
     * @since 2022-06-02 14:44:06
     */
    default String refundNotifyUrl(String api, PaymentMethodEnum paymentMethodEnum) {
        return api + "/buyer/payment/cashierRefund/notify/" + paymentMethodEnum.name();
    }
}
