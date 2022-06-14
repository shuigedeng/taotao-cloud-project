package com.taotao.cloud.payment.biz.bootx.core.payment.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelEnum;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.payment.dao.PaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.dto.payment.RefundableInfo;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayIsProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**   
* 支付记录
* @author xxm  
* @date 2021/3/8 
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentManager paymentManager;

    /**
     * 校验支付状态，支付成功则返回，支付失败/支付进行中则抛出对应的异常
     */
    public Payment getAndCheckPaymentByBusinessId(String businessId) {

        // 根据订单查询支付记录
        List<Payment> payments = paymentManager.findByBusinessIdNoCancelDesc(businessId);
        if (!CollectionUtil.isEmpty(payments)) {
            Payment  payment = payments.get(0);

            // 支付中 (非异步支付方式下)
            if (payment.getPayStatus() == PayStatusCode.TRADE_PROGRESS) {
                throw new PayIsProcessingException();
            }

            // 支付失败
            List<Integer> trades = Arrays.asList(PayStatusCode.TRADE_FAIL, PayStatusCode.TRADE_CANCEL);
            if (trades.contains(payment.getPayStatus())) {
                throw new PayFailureException("支付失败或已经被撤销");
            }

            return payment;
        }
        return null;
    }

    /**
     * 退款成功处理, 更新可退款信息
     */
    public void updateRefundSuccess(Payment payment, BigDecimal amount, PayChannelEnum payChannelEnum){
        // 删除旧有的退款记录, 替换退款完的新的
        List<RefundableInfo> refundableInfos = payment.getRefundableInfoList();
        RefundableInfo refundableInfo = refundableInfos.stream()
                .filter(o -> o.getPayChannel() == payChannelEnum.getNo())
                .findFirst()
                .orElseThrow(() -> new PayFailureException("数据不存在"));
        refundableInfos.remove(refundableInfo);
        refundableInfo.setAmount(refundableInfo.getAmount().subtract(amount));
        refundableInfos.add(refundableInfo);
        payment.setRefundableInfo(JSONUtil.toJsonStr(refundableInfos));
    }

}
