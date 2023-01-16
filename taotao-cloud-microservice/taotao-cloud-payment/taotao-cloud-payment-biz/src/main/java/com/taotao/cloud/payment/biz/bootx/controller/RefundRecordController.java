package com.taotao.cloud.payment.biz.bootx.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 退款记录
* @author xxm
* @date 2022/3/3
*/
@Tag(name = "退款记录")
@RestController
@RequestMapping("/pay/refund")
@RequiredArgsConstructor
public class RefundRecordController {

    private final RefundRecordService refundRecordService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<RefundRecordDto>> page(PageQuery PageQuery,RefundRecordDto param){
        return Res.ok(refundRecordService.page(PageQuery,param));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public ResResult<RefundRecordDto> findById(Long id){
        return Res.ok(refundRecordService.findById(id));
    }
}
