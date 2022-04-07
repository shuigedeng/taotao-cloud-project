package com.taotao.cloud.order.biz.service.order.impl;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.number.CurrencyUtil;
import com.taotao.cloud.order.api.dto.order.PriceDetailDTO;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.biz.aop.OrderLogPoint;
import com.taotao.cloud.order.biz.entity.order.Order;
import com.taotao.cloud.order.biz.entity.order.OrderItem;
import com.taotao.cloud.order.biz.mapper.order.TradeMapper;
import com.taotao.cloud.order.biz.service.order.OrderItemService;
import com.taotao.cloud.order.biz.service.order.OrderPriceService;
import com.taotao.cloud.order.biz.service.order.OrderService;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单价格业务层实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderPriceServiceImpl implements OrderPriceService {

	/**
	 * 线下收款
	 */
	@Autowired
	private BankTransferPlugin bankTransferPlugin;
	/**
	 * 订单货物
	 */
	@Autowired
	private OrderItemService orderItemService;
	/**
	 * 交易数据层
	 */
	@Resource
	private TradeMapper tradeMapper;
	/**
	 * 订单
	 */
	@Autowired
	private OrderService orderService;

	@Override
	@SystemLogPoint(description = "修改订单价格", customerLog = "'订单编号:'+#orderSn +'，价格修改为：'+#orderPrice")
	@OrderLogPoint(description = "'订单['+#orderSn+']修改价格，修改后价格为['+#orderPrice+']'", orderSn = "#orderSn")
	public Order updatePrice(String orderSn, BigDecimal orderPrice) {

		//修改订单金额
		Order order = updateOrderPrice(orderSn, orderPrice);

		//修改交易金额
		tradeMapper.updateTradePrice(order.getTradeSn());
		return order;
	}

	@Override
	@OrderLogPoint(description = "'管理员操作订单['+#orderSn+']付款'", orderSn = "#orderSn")
	public void adminPayOrder(String orderSn) {
		Order order = OperationalJudgment.judgment(orderService.getBySn(orderSn));
		//如果订单已付款，则抛出异常
		if (order.getPayStatus().equals(PayStatusEnum.PAID.name())) {
			throw new BusinessException(ResultEnum.PAY_BigDecimal_ERROR);
		}

		bankTransferPlugin.callBack(order);
	}


	/**
	 * 修改订单价格 1.判定订单是否支付 2.记录订单原始价格信息 3.计算修改的订单金额 4.修改订单价格 5.保存订单信息
	 *
	 * @param orderSn    订单编号
	 * @param orderPrice 修改订单金额
	 */
	private Order updateOrderPrice(String orderSn, BigDecimal orderPrice) {
		Order order = OperationalJudgment.judgment(orderService.getBySn(orderSn));
		//判定是否支付
		if (order.getPayStatus().equals(PayStatusEnum.PAID.name())) {
			throw new BusinessException(ResultEnum.ORDER_UPDATE_PRICE_ERROR);
		}

		//获取订单价格信息
		PriceDetailDTO orderPriceDetailDTO = order.getPriceDetailDTO();

		//修改订单价格
		order.setUpdatePrice(CurrencyUtil.sub(orderPrice, orderPriceDetailDTO.getOriginalPrice()));

		//订单修改金额=使用订单原始金额-修改后金额
		orderPriceDetailDTO.setUpdatePrice(
			CurrencyUtil.sub(orderPrice, orderPriceDetailDTO.getOriginalPrice()));
		order.setFlowPrice(orderPriceDetailDTO.getFlowPrice());
		order.setPriceDetail(JSONUtil.toJsonStr(orderPriceDetailDTO));

		//修改订单
		order.setPriceDetail(JSONUtil.toJsonStr(orderPriceDetailDTO));
		orderService.updateById(order);

		//修改子订单
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

		//获取总数，入欧最后一个则将其他orderitem的修改金额累加，然后进行扣减
		Integer index = orderItems.size();
		BigDecimal countUpdatePrice = 0D;
		for (OrderItem orderItem : orderItems) {

			//获取订单货物价格信息
			PriceDetailDTO priceDetailDTO = orderItem.getPriceDetailDTO();

			index--;
			//如果是最后一个
			if (index == 0) {
				//记录修改金额
				priceDetailDTO.setUpdatePrice(
					CurrencyUtil.sub(order.getUpdatePrice(), countUpdatePrice));
				//修改订单货物金额
				orderItem.setFlowPrice(priceDetailDTO.getFlowPrice());
				orderItem.setUnitPrice(
					CurrencyUtil.div(priceDetailDTO.getFlowPrice(), orderItem.getNum()));
				orderItem.setPriceDetail(JSONUtil.toJsonStr(priceDetailDTO));

			} else {

				//SKU占总订单 金额的百分比
				BigDecimal priceFluctuationRatio = CurrencyUtil.div(
					priceDetailDTO.getOriginalPrice(), order.getPriceDetailDTO().getOriginalPrice(),
					4);

				//记录修改金额
				priceDetailDTO.setUpdatePrice(
					CurrencyUtil.mul(order.getUpdatePrice(), priceFluctuationRatio));

				//修改订单货物金额
				orderItem.setFlowPrice(priceDetailDTO.getFlowPrice());
				orderItem.setUnitPrice(
					CurrencyUtil.div(priceDetailDTO.getFlowPrice(), orderItem.getNum()));
				orderItem.setPriceDetail(JSONUtil.toJsonStr(priceDetailDTO));
				countUpdatePrice = CurrencyUtil.add(countUpdatePrice,
					priceDetailDTO.getUpdatePrice());
			}
		}
		orderItemService.updateBatchById(orderItems);

	}

}
