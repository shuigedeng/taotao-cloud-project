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

package com.taotao.cloud.payment.biz.controller.buyer;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.payment.api.enums.PaymentClientEnum;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.kit.CashierSupport;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** 买家端,收银台接口 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-收银台接口", description = "买家端-收银台接口")
@RequestMapping("/buyer/payment/cashier")
public class CashierController {

    @Autowired
    private CashierSupport cashierSupport;

    @Operation(summary = "获取支付详情", description = "获取支付详情")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/tradeDetail")
    public Result<CashierParam> paymentParams(@Parameter(description = "客户端类型") @Validated PayParam payParam) {
        CashierParam cashierParam = cashierSupport.cashierParam(payParam);
        return Result.success(cashierParam);
    }

    @Operation(summary = "支付", description = "支付")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/pay/{paymentMethod}/{paymentClient}")
    public Result<Object> payment(
            HttpServletRequest request,
            HttpServletResponse response,
            @Parameter(description = "支付方式 WECHAT,ALIPAY") @PathVariable String paymentMethod,
            @Parameter(description = "调起方式 APP,NATIVE,JSAPI,H5,MP") @PathVariable String paymentClient,
            @Validated PayParam payParam) {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(paymentMethod);
        PaymentClientEnum paymentClientEnum = PaymentClientEnum.valueOf(paymentClient);

        try {
            return cashierSupport.payment(paymentMethodEnum, paymentClientEnum, request, response, payParam);
        } catch (ServiceException se) {
            log.info("支付异常", se);
            throw se;
        } catch (Exception e) {
            log.error("收银台支付错误", e);
        }
        return null;
    }

    @Operation(summary = "支付回调", description = "支付回调")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @RequestMapping(
            value = "/callback/{paymentMethod}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public Result<Object> callback(HttpServletRequest request, @PathVariable String paymentMethod) {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(paymentMethod);
        cashierSupport.callback(paymentMethodEnum, request);
        return Result.success(ResultEnum.PAY_SUCCESS);
    }

    @Operation(summary = "支付异步通知", description = "支付异步通知")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @RequestMapping(
            value = "/notify/{paymentMethod}",
            method = {RequestMethod.GET, RequestMethod.POST})
    public void notify(HttpServletRequest request, @PathVariable String paymentMethod) {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(paymentMethod);
        cashierSupport.notify(paymentMethodEnum, request);
    }

    @Operation(summary = "查询支付结果", description = "查询支付结果")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping(value = "/result")
    public Result<Boolean> paymentResult(PayParam payParam) {
        return Result.success(cashierSupport.paymentResult(payParam));
    }
}
