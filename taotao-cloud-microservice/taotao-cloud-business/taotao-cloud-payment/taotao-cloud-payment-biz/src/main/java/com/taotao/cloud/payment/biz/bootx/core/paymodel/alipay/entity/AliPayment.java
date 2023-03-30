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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.base.entity.BasePayment;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.alipay.AliPaymentDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝支付记录
 *
 * @author xxm
 * @date 2021/2/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_ali_payment")
public class AliPayment extends BasePayment implements EntityBaseFunction<AliPaymentDto> {

    /** 支付宝交易号 */
    private String tradeNo;

    @Override
    public AliPaymentDto toDto() {
        AliPaymentDto dto = new AliPaymentDto();
        BeanUtil.copyProperties(this, dto);
        return dto;
    }
}
