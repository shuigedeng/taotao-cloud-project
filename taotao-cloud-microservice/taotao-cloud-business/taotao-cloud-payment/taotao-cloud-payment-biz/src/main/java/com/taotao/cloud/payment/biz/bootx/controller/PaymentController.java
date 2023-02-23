package com.taotao.cloud.payment.biz.bootx.controller;

import cn.hutool.db.PageResult;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.payment.service.PaymentQueryService;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PayChannelInfo;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PaymentDto;
import com.taotao.cloud.payment.biz.bootx.param.payment.PaymentQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @author xxm
* @date 2021/6/28
*/
@Tag(name ="支付记录")
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentQueryService paymentQueryService;

    @Operation(summary = "根据id获取")
    @GetMapping("/findById")
    public ResResult<PaymentDto> findById(Long id){
        return Res.ok(paymentQueryService.findById(id));
    }

    @Operation(summary = "根据userId获取列表")
    @GetMapping("/findByUser")
    public ResResult<List<PaymentDto>> findByUser(Long userid){
        return Res.ok(paymentQueryService.findByUser(userid));
    }

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public ResResult<PageResult<PaymentDto>> page(PageQuery PageQuery, PaymentQuery param, OrderParam orderParam){
        return Res.ok(paymentQueryService.page(PageQuery,param,orderParam));
    }

    @Operation(summary = "分页查询(超级查询)")
    @PostMapping("/superPage")
    public ResResult<PageResult<PaymentDto>> superPage(PageQuery PageQuery, @RequestBody QueryParams queryParams){
        return Res.ok(paymentQueryService.superPage(PageQuery,queryParams));
    }

    @Operation(summary = "根据businessId获取列表")
    @GetMapping("/findByBusinessId")
    public ResResult<List<PaymentDto>> findByBusinessId(String businessId){
        return Res.ok(paymentQueryService.findByBusinessId(businessId));
    }

    @Operation(summary = "根据业务ID获取支付状态")
    @GetMapping("/findStatusByBusinessId")
    public ResResult<Integer> findStatusByBusinessId(String businessId){
        return Res.ok(paymentQueryService.findStatusByBusinessId(businessId));
    }

    @Operation(summary = "根据businessId获取订单支付方式")
    @GetMapping("/findPayTypeInfoByBusinessId")
    public ResResult<List<PayChannelInfo>> findPayTypeInfoByBusinessId(String businessId){
        return Res.ok(paymentQueryService.findPayTypeInfoByBusinessId(businessId));
    }
    @Operation(summary = "根据id获取订单支付方式")
    @GetMapping("/findPayTypeInfoById")
    public ResResult<List<PayChannelInfo>> findPayTypeInfoById(Long id){
        return Res.ok(paymentQueryService.findPayTypeInfoById(id));
    }

}
