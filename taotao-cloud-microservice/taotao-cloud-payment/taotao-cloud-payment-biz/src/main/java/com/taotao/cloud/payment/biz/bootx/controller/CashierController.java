package com.taotao.cloud.payment.biz.bootx.controller;

import com.taotao.cloud.payment.biz.bootx.core.cashier.service.CashierService;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import com.taotao.cloud.payment.biz.bootx.param.cashier.CashierCombinationPayParam;
import com.taotao.cloud.payment.biz.bootx.param.cashier.CashierSinglePayParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
*
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
    public ResResult<PayResult> singlePay(@RequestBody CashierSinglePayParam cashierSinglePayParam){
        return Res.ok(cashierService.singlePay(cashierSinglePayParam));
    }

    @Operation(summary = "发起支付(组合支付)")
    @PostMapping("/combinationPay")
    public ResResult<PayResult> combinationPay(@RequestBody CashierCombinationPayParam param){
        return Res.ok(cashierService.combinationPay(param));
    }

    @SneakyThrows
    @Operation(summary = "扫码聚合支付(单渠道)")
    @GetMapping("/aggregatePay")
    public ModelAndView aggregatePay(String key, HttpServletRequest request){
        String ua = request.getHeader("User-Agent");
        String payBody = cashierService.aggregatePay(key, ua);
        return new ModelAndView("redirect:"+payBody);
    }



}
