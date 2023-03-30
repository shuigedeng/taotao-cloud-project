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

package com.taotao.cloud.payment.biz.bootx.code.pay;

import java.util.Arrays;
import java.util.List;

/**
 * 支付通道
 *
 * @author xxm
 * @date 2021/2/24
 */
public interface PayChannelCode {
    /** 支付通道类型 1.支付宝 2.微信 3.云闪付 4.现金 5.钱包 6.储值卡 8.信用卡 9.ApplePay 10.渠道方支付 99.聚合支付 */
    int ALI = 1;

    int WECHAT = 2;
    int UNION_PAY = 3;
    int CASH = 4;
    int WALLET = 5;
    int VOUCHER = 6;
    int CREDIT_CARD = 8;
    int APPLE_PAY = 9;
    int CHANNEL_PAY = 10;
    int AGGREGATION = 99;

    /** 支付宝 UA */
    String UA_ALI_PAY = "Alipay";
    /** 微信 UA */
    String UA_WECHAT_PAY = "MicroMessenger";

    /** 异步支付通道 */
    List<Integer> ASYNC_TYPE = Arrays.asList(ALI, WECHAT, UNION_PAY, APPLE_PAY);
}
