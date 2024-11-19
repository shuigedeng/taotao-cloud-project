package com.taotao.cloud.payment.biz.daxpay.channel.union.service.refund;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.payment.biz.daxpay.channel.union.code.UnionPayCode;
import com.taotao.cloud.payment.biz.daxpay.channel.union.sdk.api.UnionPayKit;
import com.taotao.cloud.payment.biz.daxpay.channel.union.sdk.bean.UnionRefundOrder;
import com.taotao.cloud.payment.biz.daxpay.channel.union.sdk.bean.UnionRefundResult;
import com.taotao.cloud.payment.biz.daxpay.channel.union.service.config.UnionPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.RefundStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.service.bo.trade.RefundResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.refund.RefundOrder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 云闪付退款操作
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionRefundService {
    private final UnionPayConfigService unionPayConfigService;

    /**
     * 退款方法
     */
    public RefundResultBo refund(RefundOrder refundOrder, UnionPayKit unionPayKit) {

        BigDecimal refundAmount = refundOrder.getAmount();
        BigDecimal orderAmount = refundOrder.getOrderAmount();

        UnionRefundOrder unionRefundOrder = new UnionRefundOrder();
        unionRefundOrder.setRefundNo(refundOrder.getRefundNo());
        unionRefundOrder.setTradeNo(refundOrder.getOutOrderNo());
        unionRefundOrder.setRefundAmount(refundAmount);
        unionRefundOrder.setTotalAmount(orderAmount);
        unionRefundOrder.setNotifyUrl(unionPayConfigService.getRefundNotifyUrl());
        UnionRefundResult refund = unionPayKit.refund(unionRefundOrder);

        String outRefundNo = (String) refund.getAttr(UnionPayCode.QUERY_ID);
        // 云闪付退款是否成功需要查询状态, 所以设置为退款中状态
        return new RefundResultBo()
                .setStatus(RefundStatusEnum.PROGRESS)
                .setOutRefundNo(outRefundNo);
    }
}
