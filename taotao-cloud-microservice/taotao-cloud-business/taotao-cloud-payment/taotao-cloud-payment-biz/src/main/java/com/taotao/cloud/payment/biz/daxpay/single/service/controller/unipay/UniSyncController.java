package com.taotao.cloud.payment.biz.daxpay.single.service.controller.unipay;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.pay.PaySyncParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.refund.RefundSyncParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.transfer.TransferSyncParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.DaxResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.pay.PaySyncResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.refund.RefundSyncResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.transfer.TransferSyncResult;
import com.taotao.cloud.payment.biz.daxpay.core.util.DaxRes;
import com.taotao.cloud.payment.biz.daxpay.service.common.anno.PaymentVerify;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.pay.PaySyncService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.refund.RefundSyncService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.transfer.TransferSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一同步接口
 * @author xxm
 * @since 2024/6/4
 */
@PaymentVerify
@IgnoreAuth
@Tag(name = "统一同步接口")
@RestController
@RequestMapping("/unipay/sync/order")
@RequiredArgsConstructor
public class UniSyncController {

    private final PaySyncService paySyncService;

    private final RefundSyncService refundSyncService;

    private final TransferSyncService transferSyncService;


    @Operation(summary = "支付订单同步接口")
    @PostMapping("/pay")
    public DaxResult<PaySyncResult> pay(@RequestBody PaySyncParam param){
        return DaxRes.ok(paySyncService.sync(param));
    }

    @Operation(summary = "退款订单同步接口")
    @PostMapping("/refund")
    public DaxResult<RefundSyncResult> refund(@RequestBody RefundSyncParam param){
        return DaxRes.ok(refundSyncService.sync(param));
    }

    @Operation(summary = "转账订单同步接口")
    @PostMapping("/transfer")
    public DaxResult<TransferSyncResult> allocation(@RequestBody TransferSyncParam param){
        return DaxRes.ok(transferSyncService.sync(param));
    }
}
