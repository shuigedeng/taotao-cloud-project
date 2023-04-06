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

import com.taotao.cloud.payment.biz.bootx.core.cashier.service.CashierService;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import com.taotao.cloud.payment.biz.bootx.param.cashier.CashierCombinationPayParam;
import com.taotao.cloud.payment.biz.bootx.param.cashier.CashierSinglePayParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author xxm
 * @date 2022/2/23
 */
@Tag(name = "结算台")
@RestController
@RequestMapping("/cashier")
@RequiredArgsConstructor
public class CashierController {
    private final CashierService cashierService;

    @Operation(summary = "发起支付(单渠道)")
    @PostMapping("/singlePay")
    public ResResult<PayResult> singlePay(@RequestBody CashierSinglePayParam cashierSinglePayParam) {
        return Res.ok(cashierService.singlePay(cashierSinglePayParam));
    }

    @Operation(summary = "发起支付(组合支付)")
    @PostMapping("/combinationPay")
    public ResResult<PayResult> combinationPay(@RequestBody CashierCombinationPayParam param) {
        return Res.ok(cashierService.combinationPay(param));
    }

    @SneakyThrows
    @Operation(summary = "扫码聚合支付(单渠道)")
    @GetMapping("/aggregatePay")
    public ModelAndView aggregatePay(String key, HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        String payBody = cashierService.aggregatePay(key, ua);
        return new ModelAndView("redirect:" + payBody);
    }
}
