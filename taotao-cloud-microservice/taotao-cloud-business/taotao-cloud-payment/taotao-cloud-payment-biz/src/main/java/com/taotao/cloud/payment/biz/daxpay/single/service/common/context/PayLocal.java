package com.taotao.cloud.payment.biz.daxpay.single.service.common.context;

import cn.bootx.platform.daxpay.code.PayWayEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 异步支付信息
 * @author xxm
 * @since 2021/2/28
 */
@Data
@Accessors(chain = true)
public class PayLocal {

    /**
     * 异步支付方式
     * @see PayWayEnum
     */
    private String payWay;

    /**
     * 第三方支付网关生成的订单号, 用与将记录关联起来
     * 1. 如付款码支付直接成功时会出现
     */
    private String gatewayOrderNo;

    /** 是否支付完成 */
    private boolean payComplete;

    /** 支付参数体(通常用于发起支付的参数) */
    private String payBody;

    /** 订单失效时间, */
    private LocalDateTime expiredTime;

    /** 支付订单 */
    private PayOrder payOrder;

    /** 支付订单扩展 */
    private PayOrderExtra payOrderExtra;

    /** 通道支付订单 */
    private List<PayChannelOrder> payChannelOrders = new ArrayList<>();

}
