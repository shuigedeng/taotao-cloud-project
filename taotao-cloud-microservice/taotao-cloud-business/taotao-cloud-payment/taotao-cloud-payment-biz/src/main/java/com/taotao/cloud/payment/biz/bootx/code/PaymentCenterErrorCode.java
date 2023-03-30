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

package com.taotao.cloud.payment.biz.bootx.code;

/**
 * 错误码
 *
 * @author xxm
 * @date 2020/12/7
 */
public interface PaymentCenterErrorCode {

    // 支付过程相关
    /** 支付金额异常 */
    int PAYMENT_AMOUNT_ABNORMAL = 28100;
    /** 支付记录不存在 */
    int PAYMENT_RECORD_NOT_EXISTED = 28101;
    /** 支付在进行中 */
    int PAYMENT_IS_PROCESSING = 28102;
    /** 支付失败 */
    int PAY_FAILURE = 28103;
    /** 支付已经存在 */
    int PAYMENT_HAS_EXISTED = 28104;
    /** 支付手动取消 */
    int PAYMENT_CANCEL = 28105;
    /** 不支持的支付方式 */
    int PAYMENT_METHOD_UNSUPPORT = 28106;

    /** 钱包已存在 */
    int WALLET_ALREADY_EXISTS = 28814;

    /** 钱包不存在 */
    int WALLET_NOT_EXISTS = 28815;

    /** 钱包已被禁用 */
    int WALLET_BANNED = 28816;

    /** 钱包余额不足 */
    int WALLET_BALANCE_NOT_ENOUGH = 28817;

    /** wallet 信息不存在 */
    int WALLET_INFO_NOT_EXISTS = 28819;

    /** 钱包日志异常(类型不正确，或者充值金额小于0等场景) */
    int WALLET_LOG_ERROR = 28827;
}
