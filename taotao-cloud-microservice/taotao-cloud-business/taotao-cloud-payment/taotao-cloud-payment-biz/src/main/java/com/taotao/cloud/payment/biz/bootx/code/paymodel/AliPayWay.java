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

import com.taotao.cloud.payment.biz.bootx.code.pay.PayWayEnum;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;

/**
 * 支付宝支付方式
 *
 * @author xxm
 * @date 2021/7/2
 */
@UtilityClass
public class AliPayWay {

    // 支付方式
    private static final List<PayWayEnum> PAY_WAYS =
            Arrays.asList(PayWayEnum.WAP, PayWayEnum.APP, PayWayEnum.WEB, PayWayEnum.QRCODE, PayWayEnum.BARCODE);

    /** 根据数字编号获取 */
    public PayWayEnum findByNo(int no) {
        return PAY_WAYS.stream()
                .filter(e -> e.getNo() == no)
                .findFirst()
                .orElseThrow(() -> new PayFailureException("不存在的支付方式"));
    }
    /** 根据数字编号获取 */
    public PayWayEnum findByCode(String code) {
        return PAY_WAYS.stream()
                .filter(e -> Objects.equals(code, e.getCode()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("不存在的支付方式"));
    }

    /** 获取支持的支付方式 */
    public List<PayWayEnum> getPayWays() {
        return PAY_WAYS;
    }
}
