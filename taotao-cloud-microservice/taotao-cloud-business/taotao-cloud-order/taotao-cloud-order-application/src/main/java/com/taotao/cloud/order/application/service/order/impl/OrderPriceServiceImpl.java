/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.application.service.order.impl;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.order.application.service.order.IOrderPriceService;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单价格业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:10
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderPriceServiceImpl implements IOrderPriceService {

    /** 线下收款 */
    private final BankTransferPlugin bankTransferPlugin;
    /** 订单货物 */
    private final IOrderItemService orderItemService;
    /** 交易数据层 */
    private final ITradeService tradeService;
    /** 订单 */
    private final IOrderService orderService;

    @Override
    @SystemLogPoint(description = "修改订单价格", customerLog = "'订单编号:'+#orderSn +'，价格修改为：'+#orderPrice")
    @OrderLogPoint(description = "'订单['+#orderSn+']修改价格，修改后价格为['+#orderPrice+']'", orderSn = "#orderSn")
    public Boolean updatePrice(String orderSn, BigDecimal orderPrice) {
        // 修改订单金额
        Order order = updateOrderPrice(orderSn, orderPrice);

        // 修改交易金额
        tradeService.updateTradePrice(order.getTradeSn());
        return true;
    }

    @Override
    @OrderLogPoint(description = "'管理员操作订单['+#orderSn+']付款'", orderSn = "#orderSn")
    public Boolean adminPayOrder(String orderSn) {
        Order order = OperationalJudgment.judgment(orderService.getBySn(orderSn));
        // 如果订单已付款，则抛出异常
        if (order.getPayStatus().equals(PayStatusEnum.PAID.name())) {
            throw new BusinessException(ResultEnum.PAY_BigDecimal_ERROR);
        }

        bankTransferPlugin.callBack(order);
        return true;
    }

    /**
     * 修改订单价格 1.判定订单是否支付 2.记录订单原始价格信息 3.计算修改的订单金额 4.修改订单价格 5.保存订单信息
     *
     * @param orderSn 订单编号
     * @param orderPrice 修改订单金额
     */
    private Order updateOrderPrice(String orderSn, BigDecimal orderPrice) {
        Order order = OperationalJudgment.judgment(orderService.getBySn(orderSn));
        // 判定是否支付
        if (order.getPayStatus().equals(PayStatusEnum.PAID.name())) {
            throw new BusinessException(ResultEnum.ORDER_UPDATE_PRICE_ERROR);
        }

        // 获取订单价格信息
        PriceDetailDTO orderPriceDetailDTO = order.getPriceDetailDTO();

        // 修改订单价格
        order.setUpdatePrice(CurrencyUtils.sub(orderPrice, orderPriceDetailDTO.getOriginalPrice()));

        // 订单修改金额=使用订单原始金额-修改后金额
        orderPriceDetailDTO.setUpdatePrice(CurrencyUtils.sub(orderPrice, orderPriceDetailDTO.getOriginalPrice()));
        order.setFlowPrice(orderPriceDetailDTO.getFlowPrice());
        order.setPriceDetail(JSONUtil.toJsonStr(orderPriceDetailDTO));

        // 修改订单
        order.setPriceDetail(JSONUtil.toJsonStr(orderPriceDetailDTO));
        orderService.updateById(order);

        // 修改子订单
        updateOrderItemPrice(order);

        return order;
    }

    /**
     * 修改订单货物金额 1.计算订单货物金额在订单金额中的百分比 2.订单货物金额=订单修改后金额*订单货物百分比 3.订单货物修改价格=订单货物原始价格-订单货物修改后金额 4.修改平台佣金
     * 5.订单实际金额=修改后订单金额-平台佣金-分销提佣
     *
     * @param order 订单
     */
    private void updateOrderItemPrice(Order order) {
        List<OrderItem> orderItems = orderItemService.getByOrderSn(order.getSn());

        // 获取总数，入欧最后一个则将其他orderitem的修改金额累加，然后进行扣减
        int index = orderItems.size();
        BigDecimal countUpdatePrice = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            // 获取订单货物价格信息
            PriceDetailDTO priceDetailDTO = orderItem.getPriceDetailDTO();
            index--;

            // 如果是最后一个
            if (index == 0) {
                // 记录修改金额
                priceDetailDTO.setUpdatePrice(CurrencyUtils.sub(order.getUpdatePrice(), countUpdatePrice));
                // 修改订单货物金额
                orderItem.setFlowPrice(priceDetailDTO.getFlowPrice());
                orderItem.setUnitPrice(CurrencyUtils.div(priceDetailDTO.getFlowPrice(), orderItem.getNum()));
                orderItem.setPriceDetail(JSONUtil.toJsonStr(priceDetailDTO));
            } else {
                // SKU占总订单 金额的百分比
                BigDecimal priceFluctuationRatio = CurrencyUtils.div(
                        priceDetailDTO.getOriginalPrice(),
                        order.getPriceDetailDTO().getOriginalPrice(),
                        4);

                // 记录修改金额
                priceDetailDTO.setUpdatePrice(CurrencyUtils.mul(order.getUpdatePrice(), priceFluctuationRatio));

                // 修改订单货物金额
                orderItem.setFlowPrice(priceDetailDTO.getFlowPrice());
                orderItem.setUnitPrice(CurrencyUtils.div(priceDetailDTO.getFlowPrice(), orderItem.getNum()));
                orderItem.setPriceDetail(JSONUtil.toJsonStr(priceDetailDTO));
                countUpdatePrice = CurrencyUtils.add(countUpdatePrice, priceDetailDTO.getUpdatePrice());
            }
        }
        orderItemService.updateBatchById(orderItems);
    }
}
