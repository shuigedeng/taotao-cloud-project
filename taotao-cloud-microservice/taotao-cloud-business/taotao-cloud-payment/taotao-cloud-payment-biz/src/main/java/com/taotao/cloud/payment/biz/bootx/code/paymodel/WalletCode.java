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

package com.taotao.cloud.payment.biz.bootx.code.paymodel;

/**
 * 钱包涉及到的常量
 *
 * @author xxm
 * @date 2020/12/8
 */
public interface WalletCode {

    /** 系统操作 */
    int OPERATION_SOURCE_SYSTEM = 1;

    /** 管理员操作 */
    int OPERATION_SOURCE_ADMIN = 2;

    /** 用户操作 */
    int OPERATION_SOURCE_USER = 3;

    /** 钱包状态-正常 */
    int STATUS_NORMAL = 1;

    /** 钱包状态-禁用 */
    int STATUS_FORBIDDEN = 2;

    /** 钱包日志-开通 */
    int LOG_ACTIVE = 1;

    /** 钱包日志-主动充值 */
    int LOG_RECHARGE = 2;

    /** 钱包日志-自动充值 */
    int LOG_AUTO_RECHARGE = 3;

    /** 钱包日志-Admin余额变动 */
    int LOG_ADMIN_CHANGER = 4;

    /** 钱包日志-支付 */
    int LOG_PAY = 5;

    /** 钱包日志-系统扣除余额的日志 */
    int LOG_SYSTEM_REDUCE_BALANCE = 6;

    /** 钱包日志-退款 */
    int LOG_REFUND = 7;

    /** 钱包日志-取消支付返还 */
    int LOG_PAY_CLOSE = 8;
}
