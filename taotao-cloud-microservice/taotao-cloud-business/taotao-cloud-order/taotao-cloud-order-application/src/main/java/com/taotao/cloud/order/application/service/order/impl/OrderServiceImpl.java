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

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.enums.UserEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.stream.framework.rocketmq.tags.OrderTagsEnum;
import com.taotao.cloud.stream.framework.trigger.enums.DelayTypeEnums;
import com.taotao.cloud.stream.framework.trigger.interfaces.TimeTrigger;
import com.taotao.cloud.stream.framework.trigger.message.PintuanOrderMessage;
import com.taotao.cloud.stream.framework.trigger.model.TimeExecuteConstant;
import com.taotao.cloud.stream.framework.trigger.model.TimeTriggerMsg;
import com.taotao.cloud.stream.framework.trigger.util.DelayQueueTools;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.order.application.command.cart.dto.clientobject.OrderExportCO;
import com.taotao.cloud.order.application.command.cart.dto.TradeAddCmd.MemberAddressDTO;
import com.taotao.cloud.order.application.command.order.dto.OrderBatchDeliverAddCmd;
import com.taotao.cloud.order.application.command.order.dto.clientobject.OrderDetailCO;
import com.taotao.cloud.order.application.command.order.dto.OrderPageQry;
import com.taotao.cloud.order.application.command.order.dto.clientobject.OrderSimpleCO;
import com.taotao.cloud.order.application.command.order.dto.clientobject.OrderCO;
import com.taotao.cloud.order.application.command.order.dto.clientobject.PaymentLogCO;
import com.taotao.cloud.order.application.config.aop.order.OrderLogPoint;
import com.taotao.cloud.order.application.service.order.IOrderItemService;
import com.taotao.cloud.order.application.service.order.IOrderService;
import com.taotao.cloud.order.application.service.order.IReceiptService;
import com.taotao.cloud.order.application.service.order.IStoreFlowService;
import com.taotao.cloud.order.application.service.order.ITradeService;
import com.taotao.cloud.order.application.service.order.check.CheckService;
import com.taotao.cloud.order.application.service.order.check.ProductVO;
import com.taotao.cloud.order.application.service.trade.IOrderLogService;
import com.taotao.cloud.order.infrastructure.persistent.mapper.order.IOrderMapper;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderPO;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderLogPO;
import com.taotao.cloud.order.infrastructure.persistent.po.order.ReceiptPO;
import com.taotao.cloud.order.infrastructure.persistent.po.order.TradePO;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.web.utils.OperationalJudgment;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import zipkin2.storage.Traces;

/**
 * 子订单业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:12
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends ServiceImpl<IOrderMapper, OrderPO> implements IOrderService {

	private static final String ORDER_SN_COLUMN = "order_sn";

	/**
	 * 延时任务
	 */
	private final TimeTrigger timeTrigger;
	/**
	 * 发票
	 */
	private final IReceiptService receiptService;
	/**
	 * 订单货物 订单货物数据层
	 */
	private final IOrderItemService orderItemService;
	/**
	 * 物流公司
	 */
	private final IFeignLogisticsApi logisticsApi;
	/**
	 * 订单日志
	 */
	private final IOrderLogService orderLogService;
	/**
	 * RocketMQ
	 */
	private final RocketMQTemplate rocketMQTemplate;
	/**
	 * RocketMQ配置
	 */
	private final RocketmqCustomProperties rocketmqCustomProperties;
	/**
	 * 订单流水
	 */
	private final IStoreFlowService storeFlowService;
	/**
	 * 拼团
	 */
	private final IFeignPintuanApi feignPintuanApi;
	/**
	 * 交易服务
	 */
	private final ITradeService tradeService;

	private final CheckService checkService;

	@Override
	public Boolean intoDB(TradeDTO tradeDTO) {
		// 检查TradeDTO信息
		checkTradeDTO(tradeDTO);

		Result result = checkService.paramCheckChain(new ProductVO());

		// 存放购物车，即业务中的订单
		List<OrderPO> orderPOS = new ArrayList<>(tradeDTO.getCartList().size());
		// 存放自订单
		List<OrderItem> orderItems = new ArrayList<>();
		// 订单日志集合
		List<OrderLogPO> orderLogPOS = new ArrayList<>();
		// 订单集合
		List<OrderCO> orderCOS = new ArrayList<>();

		// 循环购物车
		tradeDTO.getCartList().forEach(item -> {
			OrderPO orderPO = new OrderPO(item, tradeDTO);
			// 构建orderVO对象
			OrderCO orderCO = new OrderCO();
			BeanUtil.copyProperties(orderPO, orderCO);
			// 持久化DO
			orderPOS.add(orderPO);
			String message = "订单[" + item.getSn() + "]创建";
			// 记录日志
			orderLogPOS.add(new OrderLogPO(
				item.getSn(),
				SecurityUtils.getUserId(),
				SecurityUtils.getCurrentUser().getType(),
				SecurityUtils.getUsername(),
				message));
			item.getCheckedSkuList()
				.forEach(sku -> orderItems.add(new OrderItem(sku, item, tradeDTO)));
			// 写入子订单信息
			orderCO.setOrderItems(orderItems);
			// orderVO 记录
			orderCOS.add(orderCO);
		});
		tradeDTO.setOrderVO(orderCOS);
		// 批量保存订单
		this.saveBatch(orderPOS);
		// 批量保存 子订单
		orderItemService.saveBatch(orderItems);
		// 批量记录订单操作日志
		orderLogService.saveBatch(orderLogPOS);
		return true;
	}

	@Override
	public IPage<OrderSimpleCO> pageQuery(OrderPageQry orderPageQry) {
		QueryWrapper<OrderSimpleCO> queryWrapper = orderPageQry.queryWrapper();
		queryWrapper.groupBy("o.id");
		queryWrapper.orderByDesc("o.id");
		return this.baseMapper.queryByParams(PageUtil.initPage(orderPageQry), queryWrapper);
	}

	@Override
	public List<OrderPO> queryListByParams(OrderPageQry orderPageQry) {
		return this.baseMapper.queryListByParams(orderPageQry.queryWrapper());
	}

	@Override
	public List<OrderPO> queryListByPromotion(
		String orderPromotionType, String payStatus, String parentOrderSn, String orderSn) {
		LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
		// 查找团长订单和已和当前拼团订单拼团的订单
		queryWrapper
			.eq(OrderPO::getOrderPromotionType, orderPromotionType)
			.eq(OrderPO::getPayStatus, payStatus)
			.and(i -> i.eq(OrderPO::getParentOrderSn, parentOrderSn)
				.or(j -> j.eq(OrderPO::getSn, orderSn)));
		return this.list(queryWrapper);
	}

	@Override
	public long queryCountByPromotion(
		String orderPromotionType, String payStatus, String parentOrderSn, String orderSn) {
		LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
		// 查找团长订单和已和当前拼团订单拼团的订单
		queryWrapper
			.eq(OrderPO::getOrderPromotionType, orderPromotionType)
			.eq(OrderPO::getPayStatus, payStatus)
			.and(i -> i.eq(OrderPO::getParentOrderSn, parentOrderSn)
				.or(j -> j.eq(OrderPO::getSn, orderSn)));
		return this.count(queryWrapper);
	}

	@Override
	public List<OrderPO> queryListByPromotion(Long pintuanId) {
		LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(OrderPO::getOrderPromotionType, PromotionTypeEnum.PINTUAN.name());
		queryWrapper.eq(OrderPO::getPromotionId, pintuanId);
		queryWrapper.nested(i -> i.eq(OrderPO::getPayStatus, PayStatusEnum.PAID.name())
			.or()
			.eq(OrderPO::getOrderStatus, OrderStatusEnum.PAID.name()));
		return this.list(queryWrapper);
	}

	@Override
	public List<OrderExportCO> queryExportOrder(OrderPageQry orderPageQry) {
		return this.baseMapper.queryExportOrder(orderPageQry.queryWrapper());
	}

	@Override
	public OrderDetailCO queryDetail(String orderSn) {
		OrderPO orderPO = this.getBySn(orderSn);
		if (orderPO == null) {
			throw new BusinessException(ResultEnum.ORDER_NOT_EXIST);
		}
		QueryWrapper<OrderItem> orderItemWrapper = new QueryWrapper<>();
		orderItemWrapper.eq(ORDER_SN_COLUMN, orderSn);
		// 查询订单项信息
		List<OrderItem> orderItems = orderItemMapper.selectList(orderItemWrapper);
		// 查询订单日志信息
		List<OrderLogPO> orderLogPOS = orderLogService.getOrderLog(orderSn);
		// 查询发票信息
		ReceiptPO receiptPO = receiptService.getByOrderSn(orderSn);
		// 查询订单和自订单，然后写入vo返回
		return new OrderDetailCO(orderPO, orderItems, orderLogPOS, receiptPO);
	}

	@Override
	@OrderLogPoint(description = "'订单['+#orderSn+']取消，原因为：'+#reason", orderSn = "#orderSn")
	public OrderPO cancel(String orderSn, String reason) {
		OrderPO orderPO = OperationalJudgment.judgment(this.getBySn(orderSn));

		// 如果订单促销类型不为空&&订单是拼团订单，并且订单未成团，则抛出异常
		if (OrderPromotionTypeEnum.PINTUAN.name().equals(orderPO.getOrderPromotionType())
			&& !orderPO.getOrderStatus().equals(OrderStatusEnum.UNDELIVERED.name())) {
			throw new BusinessException(ResultEnum.ORDER_CAN_NOT_CANCEL);
		}
		if (CharSequenceUtil.equalsAny(
			orderPO.getOrderStatus(),
			OrderStatusEnum.UNDELIVERED.name(),
			OrderStatusEnum.UNPAID.name(),
			OrderStatusEnum.PAID.name())) {

			orderPO.setOrderStatus(OrderStatusEnum.CANCELLED.name());
			orderPO.setCancelReason(reason);
			// 修改订单
			this.updateById(orderPO);
			orderStatusMessage(orderPO);
			return orderPO;
		}
		else {
			throw new BusinessException(ResultEnum.ORDER_CAN_NOT_CANCEL);
		}
	}

	@Override
	@OrderLogPoint(description = "'订单['+#orderSn+']系统取消，原因为：'+#reason", orderSn = "#orderSn")
	public void systemCancel(String orderSn, String reason) {
		OrderPO orderPO = this.getBySn(orderSn);
		orderPO.setOrderStatus(OrderStatusEnum.CANCELLED.name());
		orderPO.setCancelReason(reason);
		this.updateById(orderPO);
		orderStatusMessage(orderPO);
	}

	@Override
	public OrderPO getBySn(String orderSn) {
		return this.getOne(new LambdaQueryWrapper<OrderPO>().eq(OrderPO::getSn, orderSn));
	}

	@Override
	public void payOrder(String orderSn, String paymentMethod, String receivableNo) {
		OrderPO orderPO = this.getBySn(orderSn);
		// 如果订单已支付，就不能再次进行支付
		if (orderPO.getPayStatus().equals(PayStatusEnum.PAID.name())) {
			throw new BusinessException(ResultEnum.PAY_ERROR);
		}

		// 修改订单状态
		orderPO.setPaymentTime(LocalDateTime.now());
		orderPO.setPaymentMethod(paymentMethod);
		orderPO.setPayStatus(PayStatusEnum.PAID.name());
		orderPO.setOrderStatus(OrderStatusEnum.PAID.name());
		orderPO.setReceivableNo(receivableNo);
		orderPO.setCanReturn(
			!PaymentMethodEnum.BANK_TRANSFER.name().equals(orderPO.getPaymentMethod()));
		this.updateById(orderPO);

		// 记录订单流水
		storeFlowService.payOrder(orderSn);

		// 发送订单已付款消息
		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setOrderSn(orderPO.getSn());
		orderMessage.setPaymentMethod(paymentMethod);
		orderMessage.setNewStatus(OrderStatusEnum.PAID);
		this.sendUpdateStatusMessage(orderMessage);

		String message =
			"订单付款，付款方式[" + PaymentMethodEnum.valueOf(paymentMethod).paymentName() + "]";
		OrderLogPO orderLogPO = new OrderLogPO(orderSn, -1L, UserEnum.SYSTEM.name(), "系统操作", message);
		orderLogService.save(orderLogPO);
	}

	@Override
	@OrderLogPoint(description = "'库存确认'", orderSn = "#orderSn")
	public void afterOrderConfirm(String orderSn) {
		OrderPO orderPO = this.getBySn(orderSn);
		// 判断是否为拼团订单，进行特殊处理
		// 判断订单类型进行不同的订单确认操作
		if (OrderPromotionTypeEnum.PINTUAN.name().equals(orderPO.getOrderPromotionType())) {
			this.checkPintuanOrder(orderPO.getPromotionId(), orderPO.getParentOrderSn());
		}
		else {
			// 判断订单类型
			if (orderPO.getOrderType().equals(OrderTypeEnum.NORMAL.name())) {
				normalOrderConfirm(orderSn);
			}
			else {
				virtualOrderConfirm(orderSn);
			}
		}
	}

	@Override
	// @SystemLogPoint(description = "修改订单", customerLog = "'订单[' + #orderSn +
	// ']收货信息修改，修改为'+#memberAddressDTO.consigneeDetail+'")
	public OrderPO updateConsignee(String orderSn, MemberAddressDTO memberAddressDTO) {
		OrderPO orderPO = OperationalJudgment.judgment(this.getBySn(orderSn));

		// 要记录之前的收货地址，所以需要以代码方式进行调用 不采用注解
		String message = "订单["
			+ orderSn
			+ "]收货信息修改，由["
			+ orderPO.getConsigneeDetail()
			+ "]修改为["
			+ memberAddressDTO.getConsigneeDetail()
			+ "]";
		// 记录订单操作日志
		BeanUtil.copyProperties(memberAddressDTO, orderPO);
		this.updateById(orderPO);

		OrderLogPO orderLogPO = new OrderLogPO(
			orderSn,
			UserContext.getCurrentUser().getId(),
			UserContext.getCurrentUser().getRole().getRole(),
			UserContext.getCurrentUser().getUsername(),
			message);
		orderLogService.save(orderLogPO);

		return orderPO;
	}

	@Override
	@OrderLogPoint(description = "'订单['+#orderSn+']发货，发货单号['+#logisticsNo+']'", orderSn = "#orderSn")
	public OrderPO delivery(String orderSn, String logisticsNo, Long logisticsId) {
		OrderPO orderPO = OperationalJudgment.judgment(this.getBySn(orderSn));
		// 如果订单未发货，并且订单状态值等于待发货
		if (orderPO.getDeliverStatus().equals(DeliverStatusEnum.UNDELIVERED.name())
			&& orderPO.getOrderStatus().equals(OrderStatusEnum.UNDELIVERED.name())) {
			// 获取对应物流
			LogisticsVO logistics = logisticsApi.getById(logisticsId);
			if (logistics == null) {
				throw new BusinessException(ResultEnum.ORDER_LOGISTICS_ERROR);
			}
			// 写入物流信息
			orderPO.setLogisticsCode(logistics.getCode());
			orderPO.setLogisticsName(logistics.getName());
			orderPO.setLogisticsNo(logisticsNo);
			orderPO.setLogisticsTime(LocalDateTime.now());
			orderPO.setDeliverStatus(DeliverStatusEnum.DELIVERED.name());
			this.updateById(orderPO);
			// 修改订单状态为已发送
			this.updateStatus(orderSn, OrderStatusEnum.DELIVERED);
			// 修改订单货物可以进行售后、投诉
			orderItemService.update(new UpdateWrapper<OrderItem>()
				.eq(ORDER_SN_COLUMN, orderSn)
				.set("after_sale_status", OrderItemAfterSaleStatusEnum.NOT_APPLIED)
				.set("complain_status", OrderComplaintStatusEnum.NO_APPLY));
			// 发送订单状态改变消息
			OrderMessage orderMessage = new OrderMessage();
			orderMessage.setNewStatus(OrderStatusEnum.DELIVERED);
			orderMessage.setOrderSn(orderPO.getSn());
			this.sendUpdateStatusMessage(orderMessage);
		}
		else {
			throw new BusinessException(ResultEnum.ORDER_DELIVER_ERROR);
		}
		return orderPO;
	}

	@Override
	public Traces getTraces(String orderSn) {
		// 获取订单信息
		OrderPO orderPO = this.getBySn(orderSn);
		// 获取踪迹信息
		return logisticsApi.getLogistic(orderPO.getId(), orderPO.getLogisticsNo());
	}

	@Override
	@OrderLogPoint(description = "'订单['+#orderSn+']核销，核销码['+#verificationCode+']'", orderSn = "#orderSn")
	public OrderPO take(String orderSn, String verificationCode) {
		// 获取订单信息
		OrderPO orderPO = this.getBySn(orderSn);
		// 检测虚拟订单信息
		checkVerificationOrder(orderPO, verificationCode);
		orderPO.setOrderStatus(OrderStatusEnum.COMPLETED.name());
		// 订单完成
		this.complete(orderSn);
		return orderPO;
	}

	@Override
	public OrderPO getOrderByVerificationCode(String verificationCode) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		return this.getOne(new LambdaQueryWrapper<OrderPO>()
			.eq(OrderPO::getOrderStatus, OrderStatusEnum.TAKE.name())
			.eq(OrderPO::getStoreId, storeId)
			.eq(OrderPO::getVerificationCode, verificationCode));
	}

	@Override
	@OrderLogPoint(description = "'订单['+#orderSn+']完成'", orderSn = "#orderSn")
	public void complete(String orderSn) {
		// 是否可以查询到订单
		OrderPO orderPO = OperationalJudgment.judgment(this.getBySn(orderSn));
		complete(orderPO, orderSn);
	}

	@Override
	@OrderLogPoint(description = "'订单['+#orderSn+']完成'", orderSn = "#orderSn")
	public void systemComplete(String orderSn) {
		OrderPO orderPO = this.getBySn(orderSn);
		complete(orderPO, orderSn);
	}

	/**
	 * 完成订单方法封装
	 *
	 * @param orderPO   订单
	 * @param orderSn 订单编号
	 */
	private void complete(OrderPO orderPO, String orderSn) { // 修改订单状态为完成
		this.updateStatus(orderSn, OrderStatusEnum.COMPLETED);

		// 修改订单货物可以进行评价
		orderItemService.update(new UpdateWrapper<OrderItem>()
			.eq(ORDER_SN_COLUMN, orderSn)
			.set("comment_status", CommentStatusEnum.UNFINISHED));
		// 发送订单状态改变消息
		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setNewStatus(OrderStatusEnum.COMPLETED);
		orderMessage.setOrderSn(orderPO.getSn());
		this.sendUpdateStatusMessage(orderMessage);

		// 发送当前商品购买完成的信息（用于更新商品数据）
		List<OrderItem> orderItems = orderItemService.getByOrderSn(orderSn);
		List<GoodsCompleteMessage> goodsCompleteMessageList = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			GoodsCompleteMessage goodsCompleteMessage = new GoodsCompleteMessage();
			goodsCompleteMessage.setGoodsId(orderItem.getGoodsId());
			goodsCompleteMessage.setSkuId(orderItem.getSkuId());
			goodsCompleteMessage.setBuyNum(orderItem.getNum());
			goodsCompleteMessage.setMemberId(orderPO.getMemberId());
			goodsCompleteMessageList.add(goodsCompleteMessage);
		}
		// 发送商品购买消息
		if (!goodsCompleteMessageList.isEmpty()) {
			String destination =
				rocketmqCustomProperties.getGoodsTopic() + ":"
					+ GoodsTagsEnum.BUY_GOODS_COMPLETE.name();
			// 发送订单变更mq消息
			rocketMQTemplate.asyncSend(
				destination,
				JSONUtil.toJsonStr(goodsCompleteMessageList),
				RocketmqSendCallbackBuilder.commonCallback());
		}
	}

	@Override
	public List<OrderPO> getByTradeSn(String tradeSn) {
		LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
		return this.list(queryWrapper.eq(OrderPO::getTradeSn, tradeSn));
	}

	@Override
	public void sendUpdateStatusMessage(OrderMessage orderMessage) {
		String destination =
			rocketmqCustomProperties.getOrderTopic() + ":" + OrderTagsEnum.STATUS_CHANGE.name();
		// 发送订单变更mq消息
		rocketMQTemplate.asyncSend(
			destination, JSONUtil.toJsonStr(orderMessage),
			RocketmqSendCallbackBuilder.commonCallback());
	}

	@Override
	public void deleteOrder(String sn) {
		OrderPO orderPO = this.getBySn(sn);
		if (orderPO == null) {
			log.error("订单号为" + sn + "的订单不存在！");
			throw new BusinessException("订单号为" + sn + "的订单不存在！");
		}
		LambdaUpdateWrapper<OrderPO> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(OrderPO::getSn, sn).set(OrderPO::getDelFlag, true);
		this.update(updateWrapper);
		LambdaUpdateWrapper<OrderItem> orderItemLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		orderItemLambdaUpdateWrapper.eq(OrderItem::getOrderSn, sn).set(OrderItem::getDelFlag, true);
		this.orderItemService.update(orderItemLambdaUpdateWrapper);
	}

	@Override
	public Boolean invoice(String sn) {
		// 根据订单号查询发票信息
		ReceiptPO receiptPO = receiptService.getByOrderSn(sn);
		// 校验发票信息是否存在
		if (receiptPO != null) {
			receiptPO.setReceiptStatus(1);
			return receiptService.updateById(receiptPO);
		}
		throw new BusinessException(ResultEnum.USER_RECEIPT_NOT_EXIST);
	}

	@Override
	public void agglomeratePintuanOrder(Long pintuanId, String parentOrderSn) {
		// 获取拼团配置
		PintuanVO pintuan = feignPintuanApi.getById(pintuanId);

		List<OrderPO> list = this.getPintuanOrder(pintuanId, parentOrderSn);
		if (Boolean.TRUE.equals(pintuan.getFictitious())
			&& pintuan.getRequiredNum() > list.size()) {
			// 如果开启虚拟成团且当前订单数量不足成团数量，则认为拼团成功
			this.pintuanOrderSuccess(list);
		}
		else if (Boolean.FALSE.equals(pintuan.getFictitious())
			&& pintuan.getRequiredNum() > list.size()) {
			// 如果未开启虚拟成团且当前订单数量不足成团数量，则认为拼团失败
			this.pintuanOrderFailed(list);
		}
	}

	@Override
	public void downLoadDeliver(HttpServletResponse response, List<String> logisticsName) {
		ExcelWriter writer = ExcelUtil.getWriter();
		// Excel 头部
		ArrayList<String> rows = new ArrayList<>();
		rows.add("订单编号");
		rows.add("物流公司");
		rows.add("物流编号");
		writer.writeHeadRow(rows);

		// 存放下拉列表  ----店铺已选择物流公司列表
		String[] logiList = logisticsName.toArray(new String[]{});
		CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 200, 1, 1);
		writer.addSelect(cellRangeAddressList, logiList);

		ServletOutputStream out = null;
		try {
			// 设置公共属性，列表名称
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader(
				"Content-Disposition",
				"attachment;filename=" + URLEncoder.encode("批量发货导入模板", "UTF8") + ".xls");
			out = response.getOutputStream();
			writer.flush(out, true);
		}
		catch (Exception e) {
			log.error("获取待发货订单编号列表错误", e);
		}
		finally {
			writer.close();
			IoUtil.close(out);
		}
	}

	@Override
	public Boolean batchDeliver(MultipartFile files) {
		InputStream inputStream = null;
		List<OrderBatchDeliverAddCmd> orderBatchDeliverAddCmdList = new ArrayList<>();
		try {
			inputStream = files.getInputStream();
			// 2.应用HUtool ExcelUtil获取ExcelReader指定输入流和sheet
			ExcelReader excelReader = ExcelUtil.getReader(inputStream);
			// 可以加上表头验证
			// 3.读取第二行到最后一行数据
			List<List<Object>> read = excelReader.read(1, excelReader.getRowCount());
			for (List<Object> objects : read) {
				OrderBatchDeliverAddCmd orderBatchDeliverAddCmd = new OrderBatchDeliverAddCmd();
				orderBatchDeliverAddCmd.setOrderSn(objects.get(0).toString());
				orderBatchDeliverAddCmd.setLogisticsName(objects.get(1).toString());
				orderBatchDeliverAddCmd.setLogisticsNo(objects.get(2).toString());
				orderBatchDeliverAddCmdList.add(orderBatchDeliverAddCmd);
			}
		}
		catch (Exception e) {
			throw new BusinessException(ResultEnum.ORDER_BATCH_DELIVER_ERROR);
		}
		// 循环检查是否符合规范
		checkBatchDeliver(orderBatchDeliverAddCmdList);
		// 订单批量发货
		for (OrderBatchDeliverAddCmd orderBatchDeliverAddCmd : orderBatchDeliverAddCmdList) {
			this.delivery(
				orderBatchDeliverAddCmd.getOrderSn(),
				orderBatchDeliverAddCmd.getLogisticsNo(),
				orderBatchDeliverAddCmd.getLogisticsId());
		}
		return true;
	}

	@Override
	public BigDecimal getPaymentTotal(String orderSn) {
		OrderPO orderPO = this.getBySn(orderSn);
		TradePO tradePO = tradeService.getBySn(orderPO.getTradeSn());
		if (tradePO.getPayStatus().equals(PayStatusEnum.PAID.name())) {
			return tradePO.getFlowPrice();
		}
		return orderPO.getFlowPrice();
	}

	@Override
	public IPage<PaymentLogCO> queryPaymentLogs(IPage<PaymentLogCO> page,
		Wrapper<PaymentLogCO> queryWrapper) {
		return baseMapper.queryPaymentLogs(page, queryWrapper);
	}

	/**
	 * 循环检查批量发货订单列表
	 *
	 * @param list 待发货订单列表
	 */
	private void checkBatchDeliver(List<OrderBatchDeliverAddCmd> list) {
		List<LogisticsVO> logistics = logisticsApi.list();
		for (OrderBatchDeliverAddCmd orderBatchDeliverAddCmd : list) {
			// 查看订单号是否存在-是否是当前店铺的订单
			OrderPO orderPO = this.getOne(new LambdaQueryWrapper<OrderPO>()
				.eq(OrderPO::getStoreId, SecurityUtils.getCurrentUser().getStoreId())
				.eq(OrderPO::getSn, orderBatchDeliverAddCmd.getOrderSn()));
			if (orderPO == null) {
				throw new BusinessException(
					"订单编号：'" + orderBatchDeliverAddCmd.getOrderSn() + " '不存在");
			}
			else if (!orderPO.getOrderStatus().equals(OrderStatusEnum.UNDELIVERED.name())) {
				throw new BusinessException(
					"订单编号：'" + orderBatchDeliverAddCmd.getOrderSn() + " '不能发货");
			}
			// 获取物流公司
			logistics.forEach(item -> {
				if (item.getName().equals(orderBatchDeliverAddCmd.getLogisticsName())) {
					orderBatchDeliverAddCmd.setLogisticsId(item.getId());
				}
			});
			if (StringUtils.isEmpty(orderBatchDeliverAddCmd.getLogisticsId())) {
				throw new BusinessException(
					"物流公司：'" + orderBatchDeliverAddCmd.getLogisticsName() + " '不存在");
			}
		}
	}

	/**
	 * 订单状态变更消息
	 *
	 * @param orderPO
	 */
	private void orderStatusMessage(OrderPO orderPO) {
		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setOrderSn(orderPO.getSn());
		orderMessage.setNewStatus(OrderStatusEnum.valueOf(orderPO.getOrderStatus()));
		this.sendUpdateStatusMessage(orderMessage);
	}

	/**
	 * 此方法只提供内部调用，调用前应该做好权限处理 修改订单状态
	 *
	 * @param orderSn     订单编号
	 * @param orderStatus 订单状态
	 */
	private void updateStatus(String orderSn, OrderStatusEnum orderStatus) {
		this.baseMapper.updateStatus(orderStatus.name(), orderSn);
	}

	/**
	 * 检测拼团订单内容 此方法用与订单确认 判断拼团是否达到人数进行下一步处理
	 *
	 * @param pintuanId     拼团活动ID
	 * @param parentOrderSn 拼团父订单编号
	 */
	private void checkPintuanOrder(Long pintuanId, String parentOrderSn) {
		// 拼团有效参数判定
		if (CharSequenceUtil.isEmpty(parentOrderSn)) {
			return;
		}
		// 获取拼团配置
		PintuanVO pintuan = feignPintuanApi.getById(pintuanId);
		List<OrderPO> list = this.getPintuanOrder(pintuanId, parentOrderSn);
		int count = list.size();
		if (count == 1) {
			// 如果为开团订单，则发布一个一小时的延时任务，时间到达后，如果未成团则自动结束（未开启虚拟成团的情况下）
			PintuanOrderMessage pintuanOrderMessage = new PintuanOrderMessage();
			long startTime = DateUtil.offsetHour(new Date(), 1).getTime();
			pintuanOrderMessage.setOrderSn(parentOrderSn);
			pintuanOrderMessage.setPintuanId(pintuanId);
			TimeTriggerMsg timeTriggerMsg = new TimeTriggerMsg(
				TimeExecuteConstant.PROMOTION_EXECUTOR,
				startTime,
				pintuanOrderMessage,
				DelayQueueTools.wrapperUniqueKey(DelayTypeEnums.PINTUAN_ORDER,
					(pintuanId + parentOrderSn)),
				rocketmqCustomProperties.getPromotionTopic());

			this.timeTrigger.addDelay(timeTriggerMsg);
		}
		// 拼团所需人数，小于等于 参团后的人数，则说明成团，所有订单成团
		if (pintuan.getRequiredNum() <= count) {
			this.pintuanOrderSuccess(list);
		}
	}

	/**
	 * 根据拼团活动id和拼团订单sn获取所有当前与当前拼团订单sn相关的订单
	 *
	 * @param pintuanId     拼团活动id
	 * @param parentOrderSn 拼团订单sn
	 * @return 所有当前与当前拼团订单sn相关的订单
	 */
	private List<OrderPO> getPintuanOrder(Long pintuanId, String parentOrderSn) {
		// 寻找拼团的所有订单
		LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper
			.eq(OrderPO::getPromotionId, pintuanId)
			.eq(OrderPO::getOrderPromotionType, OrderPromotionTypeEnum.PINTUAN.name())
			.eq(OrderPO::getPayStatus, PayStatusEnum.PAID.name());
		// 拼团sn=开团订单sn 或者 参团订单的开团订单sn
		queryWrapper.and(i -> i.eq(OrderPO::getSn, parentOrderSn)
			.or(j -> j.eq(OrderPO::getParentOrderSn, parentOrderSn)));
		// 参团后的订单数（人数）
		return this.list(queryWrapper);
	}

	/**
	 * 根据提供的拼团订单列表更新拼团状态为拼团成功 循环订单列表根据不同的订单类型进行确认订单
	 *
	 * @param orderPOList 需要更新拼团状态为成功的拼团订单列表
	 */
	private void pintuanOrderSuccess(List<OrderPO> orderPOList) {
		for (OrderPO orderPO : orderPOList) {
			if (orderPO.getOrderType().equals(OrderTypeEnum.VIRTUAL.name())) {
				this.virtualOrderConfirm(orderPO.getSn());
			}
			else if (orderPO.getOrderType().equals(OrderTypeEnum.NORMAL.name())) {
				this.normalOrderConfirm(orderPO.getSn());
			}
		}
	}

	/**
	 * 根据提供的拼团订单列表更新拼团状态为拼团失败
	 *
	 * @param list 需要更新拼团状态为失败的拼团订单列表
	 */
	private void pintuanOrderFailed(List<OrderPO> list) {
		for (OrderPO orderPO : list) {
			try {
				this.systemCancel(orderPO.getSn(), "拼团人数不足，拼团失败！");
			}
			catch (Exception e) {
				log.error("拼团订单取消失败", e);
			}
		}
	}

	/**
	 * 检查交易信息
	 *
	 * @param tradeDTO 交易DTO
	 */
	private void checkTradeDTO(TradeDTO tradeDTO) {
		// 检测是否为拼团订单
		if (tradeDTO.getParentOrderSn() != null) {
			// 判断用户不能参与自己发起的拼团活动
			OrderPO parentOrderPO = this.getBySn(tradeDTO.getParentOrderSn());
			if (parentOrderPO.getMemberId().equals(SecurityUtils.getUserId())) {
				throw new BusinessException(ResultEnum.PINTUAN_JOIN_ERROR);
			}
		}
	}

	/**
	 * 检查交易信息
	 *
	 * @param orderPO 订单
	 */
	private void checkOrder(OrderPO orderPO) {
		// 订单类型为拼团订单，检测购买数量是否超过了限购数量
		if (OrderPromotionTypeEnum.PINTUAN.name().equals(orderPO.getOrderType())) {
			PintuanVO pintuan = feignPintuanApi.getById(orderPO.getPromotionId());
			Integer limitNum = pintuan.getLimitNum();
			if (limitNum != 0 && orderPO.getGoodsNum() > limitNum) {
				throw new BusinessException(ResultEnum.PINTUAN_LIMIT_NUM_ERROR);
			}
		}
	}

	/**
	 * 普通商品订单确认 修改订单状态为待发货 发送订单状态变更消息
	 *
	 * @param orderSn 订单编号
	 */
	private void normalOrderConfirm(String orderSn) {
		// 修改订单
		this.update(new LambdaUpdateWrapper<OrderPO>()
			.eq(OrderPO::getSn, orderSn)
			.set(OrderPO::getOrderStatus, OrderStatusEnum.UNDELIVERED.name()));
		// 修改订单
		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setNewStatus(OrderStatusEnum.UNDELIVERED);
		orderMessage.setOrderSn(orderSn);
		this.sendUpdateStatusMessage(orderMessage);
	}

	/**
	 * 虚拟商品订单确认 修改订单状态为待核验 发送订单状态变更消息
	 *
	 * @param orderSn 订单编号
	 */
	private void virtualOrderConfirm(String orderSn) {
		// 修改订单
		this.update(new LambdaUpdateWrapper<OrderPO>()
			.eq(OrderPO::getSn, orderSn)
			.set(OrderPO::getOrderStatus, OrderStatusEnum.TAKE.name()));
		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setNewStatus(OrderStatusEnum.TAKE);
		orderMessage.setOrderSn(orderSn);
		this.sendUpdateStatusMessage(orderMessage);
	}

	/**
	 * 检测虚拟订单信息
	 *
	 * @param orderPO            订单
	 * @param verificationCode 验证码
	 */
	private void checkVerificationOrder(OrderPO orderPO, String verificationCode) {
		// 判断查询是否可以查询到订单
		if (orderPO == null) {
			throw new BusinessException(ResultEnum.ORDER_NOT_EXIST);
		}
		// 判断是否为虚拟订单
		else if (!orderPO.getOrderType().equals(OrderTypeEnum.VIRTUAL.name())) {
			throw new BusinessException(ResultEnum.ORDER_TAKE_ERROR);
		}
		// 判断虚拟订单状态
		else if (!orderPO.getOrderStatus().equals(OrderStatusEnum.TAKE.name())) {
			throw new BusinessException(ResultEnum.ORDER_TAKE_ERROR);
		}
		// 判断验证码是否正确
		else if (!verificationCode.equals(orderPO.getVerificationCode())) {
			throw new BusinessException(ResultEnum.ORDER_TAKE_ERROR);
		}
	}
}
