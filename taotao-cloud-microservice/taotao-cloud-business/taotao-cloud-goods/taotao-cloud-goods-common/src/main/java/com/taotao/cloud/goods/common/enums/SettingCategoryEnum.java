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

package com.taotao.cloud.goods.common.enums;

/**
 * 系统设置常量
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:47:16
 */
public enum SettingCategoryEnum {
    // 基础配置
    BASE_SETTING,
    // 提现设置
    WITHDRAWAL_SETTING,
    // 分销设置
    DISTRIBUTION_SETTING,
    // 邮箱配置
    EMAIL_SETTING,
    // 商品设置
    GOODS_SETTING,
    // 快递鸟设置
    KUAIDI_SETTING,
    // 订单配置
    ORDER_SETTING,
    // 阿里OSS配置
    OSS_SETTING,
    // 阿里短信配置
    SMS_SETTING,
    // 积分设置
    POINT_SETTING,
    // 经验值设置
    EXPERIENCE_SETTING,
    // 秒杀活动设置
    SECKILL_SETTING,
    // IM 配置
    IM_SETTING,

    // 微信 联合登陆设置
    WECHAT_CONNECT,
    // QQ 浏览器 联合登录设置
    QQ_CONNECT,

    // 各端支持支付设置
    PAYMENT_SUPPORT,
    // 支付宝支付设置
    ALIPAY_PAYMENT,
    // 微信支付设置
    WECHAT_PAYMENT;
}
