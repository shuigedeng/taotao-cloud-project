package com.taotao.cloud.payment.biz.kit.params.impl;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.payment.api.enums.CashierEnum;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.params.CashierExecute;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单支付信息获取
 */
@Slf4j
@Component
public class OrderCashier implements CashierExecute {
    /**
     * 订单
     */
    @Autowired
    private OrderService orderService;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;

    @Override
    public CashierEnum cashierEnum() {
        return CashierEnum.ORDER;
    }

    @Override
    public CashierParam getPaymentParams(PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.ORDER.name())) {
            //准备返回的数据
            CashierParam cashierParam = new CashierParam();
            //订单信息获取
            OrderDetailVO order = orderService.queryDetail(payParam.getSn());

            //如果订单已支付，则不能发器支付
            if (order.getOrder().getPayStatus().equals(PayStatusEnum.PAID.name())) {
                throw new BusinessException(ResultEnum.PAY_BigDecimal_ERROR);
            }
            //如果订单状态不是待付款，则抛出异常
            if (!order.getOrder().getOrderStatus().equals(OrderStatusEnum.UNPAID.name())) {
                throw new BusinessException(ResultEnum.PAY_BAN);
            }
            cashierParam.setPrice(order.getOrder().getFlowPrice());

            try {
                BaseSetting baseSetting = JSONUtil.toBean(settingService.get(SettingEnum.BASE_SETTING.name()).getSettingValue(), BaseSetting.class);
                cashierParam.setTitle(baseSetting.getSiteName());
            } catch (Exception e) {
                cashierParam.setTitle("多用户商城，在线支付");
            }


            List<OrderItem> orderItemList = order.getOrderItems();
            StringBuilder subject = new StringBuilder();
            for (OrderItem orderItem : orderItemList) {
                subject.append(orderItem.getGoodsName()).append(";");
            }

            cashierParam.setDetail(subject.toString());

            cashierParam.setOrderSns(payParam.getSn());
            cashierParam.setCreateTime(order.getOrder().getCreateTime());
            return cashierParam;
        }

        return null;
    }

    @Override
    public void paymentSuccess(PaymentSuccessParams paymentSuccessParams) {

        PayParam payParam = paymentSuccessParams.getPayParam();
        if (payParam.getOrderType().equals(CashierEnum.ORDER.name())) {
            orderService.payOrder(payParam.getSn(),
                    paymentSuccessParams.getPaymentMethod(),
                    paymentSuccessParams.getReceivableNo());
            log.info("订单{}支付成功,金额{},方式{}", payParam.getSn(),
                    paymentSuccessParams.getPaymentMethod(),
                    paymentSuccessParams.getReceivableNo());
        }
    }

    @Override
    public Boolean paymentResult(PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.ORDER.name())) {
            Order order = orderService.getBySn(payParam.getSn());
            if (order != null) {
                return PayStatusEnum.PAID.name().equals(order.getPayStatus());
            } else {
                throw new BusinessException(ResultEnum.PAY_NOT_EXIST_ORDER);
            }
        }
        return false;
    }
}
