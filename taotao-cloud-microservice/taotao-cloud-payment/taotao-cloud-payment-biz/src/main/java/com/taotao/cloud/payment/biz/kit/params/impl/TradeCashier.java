package com.taotao.cloud.payment.biz.kit.params.impl;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 整笔交易信息获取
 */
@Slf4j
@Component
public class TradeCashier implements CashierExecute {

    /**
     * 交易
     */
    @Autowired
    private TradeService tradeService;
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
        return CashierEnum.TRADE;
    }

    @Override
    public CashierParam getPaymentParams(PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.TRADE.name())) {
            //准备返回的数据
            CashierParam cashierParam = new CashierParam();
            //订单信息获取
            Trade trade = tradeService.getBySn(payParam.getSn());

            List<Order> orders = orderService.getByTradeSn(payParam.getSn());


            String orderSns = orders.stream().map(Order::getSn).collect(Collectors.joining(", "));
            cashierParam.setOrderSns(orderSns);

            for (Order order : orders) {
                //如果订单已支付，则不能发器支付
                if (order.getPayStatus().equals(PayStatusEnum.PAID.name())) {
                    throw new BusinessException(ResultEnum.PAY_PARTIAL_ERROR);
                }
                //如果订单状态不是待付款，则抛出异常
                if (!order.getOrderStatus().equals(OrderStatusEnum.UNPAID.name())) {
                    throw new BusinessException(ResultEnum.PAY_BAN);
                }
            }


            cashierParam.setPrice(trade.getFlowPrice());

            try {
                BaseSetting baseSetting = JSONUtil.toBean(settingService.get(SettingEnum.BASE_SETTING.name()).getSettingValue(), BaseSetting.class);
                cashierParam.setTitle(baseSetting.getSiteName());
            } catch (Exception e) {
                cashierParam.setTitle("多用户商城，在线支付");
            }
            String subject = "在线支付";
            cashierParam.setDetail(subject);

            cashierParam.setCreateTime(trade.getCreateTime());
            return cashierParam;
        }

        return null;
    }


    @Override
    public void paymentSuccess(PaymentSuccessParams paymentSuccessParams) {
        if (paymentSuccessParams.getPayParam().getOrderType().equals(CashierEnum.TRADE.name())) {
            tradeService.payTrade(paymentSuccessParams.getPayParam().getSn(),
                    paymentSuccessParams.getPaymentMethod(),
                    paymentSuccessParams.getReceivableNo());
            log.info("交易{}支付成功,方式{},流水号{},", paymentSuccessParams.getPayParam().getSn(),
                    paymentSuccessParams.getPaymentMethod(),
                    paymentSuccessParams.getReceivableNo());
        }
    }

    @Override
    public Boolean paymentResult(PayParam payParam) {

        if (payParam.getOrderType().equals(CashierEnum.TRADE.name())) {
            Trade trade = tradeService.getBySn(payParam.getSn());
            if (trade != null) {
                return PayStatusEnum.PAID.name().equals(trade.getPayStatus());
            } else {
                throw new BusinessException(ResultEnum.PAY_NOT_EXIST_ORDER);
            }
        }
        return false;
    }
}
