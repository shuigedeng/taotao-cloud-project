package com.taotao.cloud.payment.biz.bootx.controller;

import com.taotao.cloud.payment.biz.bootx.core.aggregate.service.AggregateService;
import com.taotao.cloud.payment.biz.bootx.param.cashier.CashierSinglePayParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 聚合支付
* @author xxm
* @date 2022/3/6
*/
@Tag(name = "聚合支付")
@RestController
@RequestMapping("/aggregate")
@RequiredArgsConstructor
public class AggregateController {
    private final AggregateService aggregateService;

    @Operation(summary = "创建聚合支付")
    @PostMapping("/createAggregatePay")
    public ResResult<String> createAggregatePay(@RequestBody CashierSinglePayParam param){
        return Res.ok(aggregateService.createAggregatePay(param));
    }
}
