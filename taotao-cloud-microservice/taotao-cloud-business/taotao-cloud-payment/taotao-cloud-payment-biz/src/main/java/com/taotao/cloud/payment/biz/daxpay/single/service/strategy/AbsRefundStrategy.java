package com.taotao.cloud.payment.biz.daxpay.single.service.strategy;

import com.taotao.cloud.payment.biz.daxpay.service.bo.trade.RefundResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.refund.RefundOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象支付退款策略
 *
 * @author xxm
 * @since 2020/12/11
 */
@Getter
@Setter
public abstract class AbsRefundStrategy implements PaymentStrategy{

    /** 退款订单 */
    private RefundOrder refundOrder = null;

    /**
     * 退款前对处理, 主要进行各种检查
     */
    public void doBeforeRefundHandler() {
    }

    /**
     * 退款操作
     */
    public abstract RefundResultBo doRefundHandler();

}
