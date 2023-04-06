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

package com.taotao.cloud.payment.biz.bootx.controller;

import com.taotao.cloud.payment.biz.bootx.core.pay.service.PayCancelService;
import com.taotao.cloud.payment.biz.bootx.core.pay.service.PayRefundService;
import com.taotao.cloud.payment.biz.bootx.core.pay.service.PayService;
import com.taotao.cloud.payment.biz.bootx.core.pay.service.PaySyncService;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PaymentDto;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import com.taotao.cloud.payment.biz.bootx.param.refund.RefundParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @date 2020/12/9
 */
@Tag(name = "统一支付")
@RestController
@RequestMapping("/uni_pay")
@AllArgsConstructor
public class PayController {
    private final PayService payService;
    private final PayCancelService payCancelService;
    private final PayRefundService payRefundService;
    private final PaySyncService paySyncService;

    @Operation(summary = "支付")
    @PostMapping("/pay")
    public ResResult<PayResult> pay(@RequestBody PayParam payParam) {
        return Res.ok(payService.pay(payParam));
    }

    @Operation(summary = "取消支付(支付id)")
    @PostMapping("/cancelByPaymentId")
    public ResResult<Void> cancelByPaymentId(@Parameter(description = "支付id") Long paymentId) {
        payCancelService.cancelByPaymentId(paymentId);
        return Res.ok();
    }

    @Operation(summary = "取消支付(业务id)")
    @PostMapping("/cancelByBusinessId")
    public ResResult<Void> cancelByBusinessId(@Parameter(description = "业务id") String businessId) {
        payCancelService.cancelByBusinessId(businessId);
        return Res.ok();
    }

    @Operation(summary = "刷新指定业务id的支付单状态")
    @PostMapping("/syncByBusinessId")
    public ResResult<PaymentDto> syncByBusinessId(@Parameter(description = "业务id") String businessId) {
        return Res.ok(paySyncService.syncByBusinessId(businessId));
    }

    @Operation(summary = "退款")
    @PostMapping("/refund")
    public ResResult<Void> refund(@RequestBody RefundParam refundParam) {
        payRefundService.refund(refundParam);
        return Res.ok();
    }

    @Operation(summary = "全额退款(业务id)")
    @PostMapping("/refundByBusinessId")
    public ResResult<Void> refundByBusinessId(@Parameter(description = "业务id") String businessId) {
        payRefundService.refundByBusinessId(businessId);
        return Res.ok();
    }
}
