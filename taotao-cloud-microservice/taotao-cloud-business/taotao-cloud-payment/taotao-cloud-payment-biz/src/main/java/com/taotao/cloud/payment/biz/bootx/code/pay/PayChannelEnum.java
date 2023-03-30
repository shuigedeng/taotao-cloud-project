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

import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付通道枚举
 *
 * @author xxm
 * @date 2021/7/26
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {
    ALI(PayChannelCode.ALI, "ALI"),
    WECHAT(PayChannelCode.WECHAT, "WECHAT"),
    UNION_PAY(PayChannelCode.UNION_PAY, "UNION_PAY"),
    CASH(PayChannelCode.CASH, "CASH"),
    WALLET(PayChannelCode.WALLET, "WALLET"),
    VOUCHER(PayChannelCode.VOUCHER, "VOUCHER"),
    CREDIT_CARD(PayChannelCode.CREDIT_CARD, "CREDIT"),
    APPLE_PAY(PayChannelCode.APPLE_PAY, "APPLE"),
    CHANNEL_PAY(PayChannelCode.CHANNEL_PAY, "CHANNEL"),
    AGGREGATION(PayChannelCode.AGGREGATION, "AGGREGATION");

    /** 支付通道数字编码 */
    private final int no;
    /** 支付通道字符编码 */
    private final String code;

    /** 根据数字编号获取 */
    public static PayChannelEnum findByNo(int no) {
        return Arrays.stream(PayChannelEnum.values())
                .filter(e -> e.getNo() == no)
                .findFirst()
                .orElseThrow(() -> new PayFailureException("不存在的支付通道"));
    }
    /** 根据字符编码获取 */
    public static PayChannelEnum findByCode(String code) {
        return Arrays.stream(PayChannelEnum.values())
                .filter(e -> Objects.equals(code, e.getCode()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("不存在的支付通道"));
    }

    public static boolean existsByCode(String code) {
        return Arrays.stream(PayChannelEnum.values())
                .anyMatch(payChannelEnum -> Objects.equals(payChannelEnum.getCode(), code));
    }
}
