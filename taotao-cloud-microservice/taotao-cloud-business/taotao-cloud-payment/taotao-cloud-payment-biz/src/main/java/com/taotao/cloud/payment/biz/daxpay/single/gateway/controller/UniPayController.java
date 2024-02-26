package com.taotao.cloud.payment.biz.daxpay.single.gateway.controller;

import cn.bootx.platform.daxpay.result.DaxResult;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.bootx.platform.daxpay.result.pay.RefundResult;
import cn.bootx.platform.daxpay.result.pay.SyncResult;
import cn.bootx.platform.daxpay.util.DaxRes;
import com.taotao.cloud.payment.biz.daxpay.single.core.code.PaymentApiCode;
import com.taotao.cloud.payment.biz.daxpay.single.service.annotation.PaymentApi;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.close.service.PayCloseService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.refund.service.RefundService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.sync.service.PaySyncService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.sync.service.RefundSyncService;
import com.taotao.cloud.web.annotation.CountTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一支付接口
 * @author xxm
 * @since 2023/12/15
 */
@IgnoreAuth
@Tag(name = "统一支付接口")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UniPayController {
    private final PayService payService;
    private final RefundService refundService;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;
    private final RefundSyncService refundSyncService;


    @CountTime
    @PaymentApi(PaymentApiCode.PAY)
    @Operation(summary = "统一支付接口")
    @PostMapping("/pay")
    public DaxResult<PayResult> pay(@RequestBody PayParam payParam){
        return DaxRes.ok(payService.pay(payParam));
    }

    @CountTime
    @PaymentApi(PaymentApiCode.SIMPLE_PAY)
    @Operation(summary = "简单支付接口")
    @PostMapping("/simplePay")
    public DaxResult<PayResult> simplePay(@RequestBody SimplePayParam payParam){
        return DaxRes.ok(payService.simplePay(payParam));
    }

    @CountTime
    @PaymentApi(PaymentApiCode.CLOSE)
    @Operation(summary = "支付关闭接口")
    @PostMapping("/close")
    public DaxResult<Void> close(@RequestBody PayCloseParam param){
        payCloseService.close(param);
        return DaxRes.ok();
    }

    @CountTime
    @PaymentApi(PaymentApiCode.REFUND)
    @Operation(summary = "统一退款接口")
    @PostMapping("/refund")
    public DaxResult<RefundResult> refund(@RequestBody RefundParam param){
        return DaxRes.ok(refundService.refund(param));
    }

    @CountTime
    @PaymentApi(PaymentApiCode.SIMPLE_REFUND)
    @Operation(summary = "简单退款接口")
    @PostMapping("/simpleRefund")
    public DaxResult<RefundResult> simpleRefund(@RequestBody SimpleRefundParam param){
        return DaxRes.ok(refundService.simpleRefund(param));
    }

    @CountTime
    @PaymentApi(PaymentApiCode.SYNC_PAY)
    @Operation(summary = "支付同步接口")
    @PostMapping("/syncPay")
    public DaxResult<SyncResult> syncPay(@RequestBody PaySyncParam param){
        return DaxRes.ok(paySyncService.sync(param));
    }

    @CountTime
    @PaymentApi(PaymentApiCode.SYNC_REFUND)
    @Operation(summary = "退款同步接口")
    @PostMapping("/syncRefund")
    public DaxResult<SyncResult> syncRefund(@RequestBody RefundSyncParam param){
        return DaxRes.ok(refundSyncService.sync(param));
    }

}
