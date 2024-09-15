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

package com.taotao.cloud.order.application.service.aftersale.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.order.application.command.aftersale.dto.clientobject.AfterSaleApplyCO;
import com.taotao.cloud.order.application.command.aftersale.dto.AfterSaleAddCmd;
import com.taotao.cloud.order.application.command.aftersale.dto.AfterSalePageQry;
import com.taotao.cloud.order.application.config.aop.aftersale.AfterSaleLogPoint;
import com.taotao.cloud.order.application.service.aftersale.IAfterSaleService;
import com.taotao.cloud.order.application.service.order.IOrderItemService;
import com.taotao.cloud.order.application.service.order.IOrderService;
import com.taotao.cloud.order.infrastructure.persistent.mapper.aftersale.IAfterSaleMapper;
import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSalePO;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.common.utils.common.IdGeneratorUtils;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.boot.common.utils.number.NumberUtils;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.AfterSaleTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.hutool.core.text.CharSequenceUtil;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 售后业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:30
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AfterSaleServiceImpl extends ServiceImpl<IAfterSaleMapper, AfterSalePO> implements
	IAfterSaleService {
	private final AfterSaleManager afterSaleManager;

	/**
	 * 订单
	 */
	private final IOrderService orderService;
	/**
	 * 订单货物
	 */
	private final IOrderItemService orderItemService;
	/**
	 * 物流公司
	 */
	private final IFeignLogisticsApi logisticsApi;
	/**
	 * 店铺详情
	 */
	private final IFeignStoreDetailApi storeDetailApi;
	/**
	 * 售后支持，这里用于退款操作
	 */
	private final IFeignRefundSupportApi refundSupportApi;
	/**
	 * RocketMQ配置
	 */
	private final RocketmqCustomProperties rocketmqCustomProperties;
	/**
	 * RocketMQ
	 */
	private final RocketMQTemplate rocketMQTemplate;

	@Override
	public IPage<AfterSalePO> pageQuery(AfterSalePageQry afterSalePageQry) {
		return baseMapper.queryByParams(
			afterSalePageQry.buildMpPage(), afterSaleManager.queryWrapper(afterSalePageQry));
	}

	@Override
	public List<AfterSalePO> exportAfterSaleOrder(AfterSalePageQry afterSalePageQry) {
		return this.list(afterSaleManager.queryWrapper(afterSalePageQry));
	}

	@Override
	public AfterSalePO getAfterSaleBySn(String sn) {
		LambdaQueryWrapper<AfterSalePO> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(AfterSalePO::getSn, sn);
		return this.getOne(queryWrapper);
	}

	@Override
	public AfterSaleApplyCO getAfterSaleVO(String sn) {
		AfterSaleApplyVOBuilder afterSaleApplyVOBuilder = AfterSaleApplyVOBuilder.builder();

		// 获取订单货物判断是否可申请售后
		OrderItem orderItem = orderItemService.getBySn(sn);

		// 未申请售后订单货物才能进行申请
		if (!orderItem.getAfterSaleStatus().equals(OrderItemAfterSaleStatusEnum.NOT_APPLIED.name())) {
			throw new BusinessException(ResultEnum.AFTER_SALES_BAN);
		}

		// 获取售后类型
		Order order = OperationalJudgment.judgment(orderService.getBySn(orderItem.getOrderSn()));

		// 订单未支付，不能申请申请售后
		if (order.getPaymentMethod() == null) {
			throw new BusinessException(ResultEnum.AFTER_SALES_NOT_PAY_ERROR);
		}
		// 判断支付方式是否为线上支付
		if (order.getPaymentMethod().equals(PaymentMethodEnum.BANK_TRANSFER.name())) {
			afterSaleApplyVOBuilder.refundWay(AfterSaleRefundWayEnum.OFFLINE.name());
		} else {
			afterSaleApplyVOBuilder.refundWay(AfterSaleRefundWayEnum.ORIGINAL.name());
		}
		// 判断订单类型，虚拟订单只支持退款
		if (order.getOrderType().equals(OrderTypeEnum.VIRTUAL.name())) {
			afterSaleApplyVOBuilder.returnMoney(true);
			afterSaleApplyVOBuilder.returnGoods(false);
		} else {
			afterSaleApplyVOBuilder.returnMoney(true);
			afterSaleApplyVOBuilder.returnGoods(true);
		}

		afterSaleApplyVOBuilder.accountType(order.getPaymentMethod());
		afterSaleApplyVOBuilder.applyRefundPrice(CurrencyUtils.div(orderItem.getFlowPrice(), orderItem.getNum()));
		afterSaleApplyVOBuilder.num(orderItem.getNum());
		afterSaleApplyVOBuilder.goodsId(orderItem.getGoodsId());
		afterSaleApplyVOBuilder.goodsName(orderItem.getGoodsName());
		afterSaleApplyVOBuilder.image(orderItem.getImage());
		afterSaleApplyVOBuilder.goodsPrice(orderItem.getGoodsPrice());
		afterSaleApplyVOBuilder.skuId(orderItem.getSkuId());
		afterSaleApplyVOBuilder.memberId(order.getMemberId());
		return afterSaleApplyVOBuilder.build();
	}

	@Override
	@AfterSaleLogPoint(sn = "#rvt.sn", description = "'售后申请:售后编号['+#rvt.sn+']'")
	public Boolean saveAfterSale(AfterSaleAddCmd afterSaleAddCmd) {
		// 检查当前订单是否可申请售后
		this.checkAfterSaleType(afterSaleAddCmd);
		// 添加售后
		addAfterSale(afterSaleAddCmd);
		return true;
	}

	@AfterSaleLogPoint(sn = "#afterSaleSn", description = "'审核售后:售后编号['+#afterSaleSn+']，'+ #serviceStatus")
	// @SystemLogPoint(description = "售后-审核售后", customerLog = "'审核售后:售后编号['+#afterSaleSn+']，'+
	// #serviceStatus")
	@Override
	public Boolean review(String afterSaleSn, String serviceStatus, String remark, BigDecimal actualRefundPrice) {
		// 根据售后单号获取售后单
		AfterSalePO afterSale = OperationalJudgment.judgment(this.getBySn(afterSaleSn));

		// 判断为待审核的售后服务
		if (!afterSale.getServiceStatus().equals(AfterSaleStatusEnum.APPLY.name())) {
			throw new BusinessException(ResultEnum.AFTER_SALES_PRICE_ERROR);
		}
		// 判断退款金额与付款金额是否正确,退款金额不能大于付款金额
		if (NumberUtils.compare(afterSale.getFlowPrice(), actualRefundPrice) < 0) {
			throw new BusinessException(ResultEnum.AFTER_SALES_PRICE_ERROR);
		}
		afterSale.setActualRefundPrice(actualRefundPrice);
		// 判断审核状态
		// 如果售后类型为：退款，审核状态为已通过并且退款方式为原路退回，售后单状态为已完成。
		// 如果售后类型为：退款，审核状态已通过并且退款方式为线下退回，售后单状态为待退款。
		// 如果售后类型不为退款，售后单状态为：已通过。
		AfterSaleStatusEnum afterSaleStatusEnum;
		if (serviceStatus.equals(AfterSaleStatusEnum.PASS.name())) {
			if (afterSale.getServiceType().equals(AfterSaleTypeEnum.RETURN_MONEY.name())) {
				if (afterSale.getRefundWay().equals(AfterSaleRefundWayEnum.ORIGINAL.name())) {
					// 如果为退款操作 && 在线支付 则直接进行退款
					refundSupportApi.refund(afterSale.getSn());
					afterSale.setRefundTime(LocalDateTime.now());
					afterSaleStatusEnum = AfterSaleStatusEnum.COMPLETE;
				} else {
					afterSaleStatusEnum = AfterSaleStatusEnum.WAIT_REFUND;
				}
			} else {
				afterSaleStatusEnum = AfterSaleStatusEnum.PASS;
			}
		} else {
			afterSaleStatusEnum = AfterSaleStatusEnum.REFUSE;
		}
		afterSale.setServiceStatus(afterSaleStatusEnum.name());
		afterSale.setAuditRemark(remark);

		// 根据售后编号修改售后单
		this.updateAfterSale(afterSaleSn, afterSale);
		// 根据售后状态。修改OrderItem订单中正在售后商品数量及状态
		this.updateOrderItemAfterSaleStatus(afterSale);
		// 发送售后消息
		this.sendAfterSaleMessage(afterSale);

		return true;
	}

	@AfterSaleLogPoint(sn = "#afterSaleSn", description = "'买家退货,物流填写:单号['+#afterSaleSn+']，物流单号为['+#logisticsNo+']'")
	// @SystemLogPoint(description = "售后-买家退货,物流填写", customerLog =
	// "'买家退货,物流填写:单号['+#afterSaleSn+']，物流单号为['+#logisticsNo+']'")
	@Override
	public AfterSalePO buyerDelivery(
		String afterSaleSn, String logisticsNo, Long logisticsId, LocalDateTime mDeliverTime) {
		// 根据售后单号获取售后单
		AfterSalePO afterSale = OperationalJudgment.judgment(this.getBySn(afterSaleSn));

		// 判断为已审核通过，待邮寄的售后服务
		if (!afterSale.getServiceStatus().equals(AfterSaleStatusEnum.PASS.name())) {
			throw new BusinessException(ResultEnum.AFTER_STATUS_ERROR);
		}

		// 查询会员回寄的物流公司信息
		LogisticsVO logistics = logisticsApi.getById(logisticsId);

		// 判断物流公司是否为空
		if (logistics == null) {
			throw new BusinessException(ResultEnum.AFTER_STATUS_ERROR);
		}

		afterSale.setLogisticsCode(logistics.getCode());
		afterSale.setLogisticsName(logistics.getName());
		afterSale.setLogisticsNo(logisticsNo);
		afterSale.setDeliverTime(mDeliverTime);
		// 修改售后单状态
		afterSale.setServiceStatus(AfterSaleStatusEnum.BUYER_RETURN.name());

		// 根据售后编号修改售后单
		this.updateAfterSale(afterSaleSn, afterSale);
		return afterSale;
	}

	@Override
	public TracesVO deliveryTraces(String afterSaleSn) {
		// 根据售后单号获取售后单
		AfterSalePO afterSale = OperationalJudgment.judgment(this.getBySn(afterSaleSn));

		return logisticsApi.getLogistic(afterSale.getId(), afterSale.getLogisticsNo());
	}

	@Override
	@AfterSaleLogPoint(
		sn = "#afterSaleSn",
		description = "'售后-商家收货:单号['+#afterSaleSn+']，物流单号为['+#logisticsNo+']"
			+ ",处理结果['+serviceStatus='PASS'?'商家收货':'商家拒收'+']'")
	// @SystemLogPoint(description = "售后-商家收货", customerLog =
	//	"'售后-商家收货:单号['+#afterSaleSn+']，物流单号为['+#logisticsNo+']" +
	//		",处理结果['+serviceStatus='PASS'?'商家收货':'商家拒收'+']'")
	public Boolean storeConfirm(String afterSaleSn, String serviceStatus, String remark) {
		// 根据售后单号获取售后单
		AfterSalePO afterSale = OperationalJudgment.judgment(this.getBySn(afterSaleSn));

		// 判断是否为已邮寄售后单
		if (!afterSale.getServiceStatus().equals(AfterSaleStatusEnum.BUYER_RETURN.name())) {
			throw new BusinessException(ResultEnum.AFTER_STATUS_ERROR);
		}
		AfterSaleStatusEnum afterSaleStatusEnum;
		String pass = "PASS";
		// 判断审核状态
		// 在线支付 则直接进行退款
		if (pass.equals(serviceStatus) && afterSale.getRefundWay().equals(AfterSaleRefundWayEnum.ORIGINAL.name())) {
			refundSupportApi.refund(afterSale.getSn());
			afterSaleStatusEnum = AfterSaleStatusEnum.COMPLETE;
		} else if (pass.equals(serviceStatus)) {
			afterSaleStatusEnum = AfterSaleStatusEnum.WAIT_REFUND;
		} else {
			afterSaleStatusEnum = AfterSaleStatusEnum.SELLER_TERMINATION;
		}
		afterSale.setServiceStatus(afterSaleStatusEnum.name());
		afterSale.setAuditRemark(remark);

		// 根据售后编号修改售后单
		this.updateAfterSale(afterSaleSn, afterSale);
		// 根据售后状态。修改OrderItem订单中正在售后商品数量及状态
		this.updateOrderItemAfterSaleStatus(afterSale);
		// 发送售后消息
		this.sendAfterSaleMessage(afterSale);
		return true;
	}

	@Override
	@AfterSaleLogPoint(sn = "#afterSaleSn", description = "'售后-平台退款:单号['+#afterSaleSn+']，备注为['+#remark+']'")
	// @SystemLogPoint(description = "售后-平台退款", customerLog =
	// "'售后-平台退款:单号['+#afterSaleSn+']，备注为['+#remark+']'")
	public Boolean refund(String afterSaleSn, String remark) {
		// 根据售后单号获取售后单
		AfterSalePO afterSale = OperationalJudgment.judgment(this.getBySn(afterSaleSn));
		afterSale.setServiceStatus(AfterSaleStatusEnum.COMPLETE.name());
		// 根据售后编号修改售后单
		this.updateAfterSale(afterSaleSn, afterSale);
		// 退款
		refundSupportApi.refund(afterSale.getSn());
		// 发送退款消息
		this.sendAfterSaleMessage(afterSale);
		return true;
	}

	@Override
	@AfterSaleLogPoint(sn = "#afterSaleSn", description = "'售后-买家确认解决:单号['+#afterSaleSn+']'")
	// @SystemLogPoint(description = "售后-买家确认解决", customerLog = "'售后-买家确认解决:单号['+#afterSaleSn+']'")
	public AfterSalePO complete(String afterSaleSn) {
		AfterSalePO afterSale = this.getBySn(afterSaleSn);
		afterSale.setServiceStatus(AfterSaleStatusEnum.COMPLETE.name());
		this.updateAfterSale(afterSaleSn, afterSale);
		return afterSale;
	}

	@Override
	@AfterSaleLogPoint(sn = "#afterSaleSn", description = "'售后-买家取消:单号['+#afterSaleSn+']'")
	// @SystemLogPoint(description = "售后-取消售后", customerLog = "'售后-买家取消:单号['+#afterSaleSn+']'")
	public Boolean cancel(String afterSaleSn) {
		// 根据售后单号获取售后单
		AfterSalePO afterSale = OperationalJudgment.judgment(this.getBySn(afterSaleSn));

		// 判断售后单是否可以申请售后
		// 如果售后状态为：待审核、已通过则可进行申请售后
		if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.APPLY.name())
			|| afterSale.getServiceStatus().equals(AfterSaleStatusEnum.PASS.name())) {

			afterSale.setServiceStatus(AfterSaleStatusEnum.BUYER_CANCEL.name());

			// 根据售后编号修改售后单
			this.updateAfterSale(afterSaleSn, afterSale);
			// 根据售后状态。修改OrderItem订单中正在售后商品数量及状态
			this.updateOrderItemAfterSaleStatus(afterSale);
			return true;
		}
		throw new BusinessException(ResultEnum.AFTER_SALES_CANCEL_ERROR);
	}

	@Override
	public StoreAfterSaleAddressVO getStoreAfterSaleAddressVO(String sn) {
		return storeDetailApi.getStoreAfterSaleAddressDTO(
			OperationalJudgment.judgment(this.getBySn(sn)).getStoreId());
	}

	/**
	 * 创建售后
	 *
	 * @param afterSaleAddCmd 售后
	 * @return 售后
	 */
	private AfterSalePO addAfterSale(AfterSaleAddCmd afterSaleAddCmd) {
		// 写入其他属性
		SecurityUser user = SecurityUtils.getCurrentUser();

		AfterSalePO afterSale = new AfterSalePO();
		BeanUtils.copyProperties(afterSaleAddCmd, afterSale);

		// 写入会员信息
		afterSale.setMemberId(user.getUserId());
		afterSale.setMemberName(user.getNickname());

		// 写入商家信息
		OrderItem orderItem = orderItemService.getBySn(afterSaleAddCmd.orderItemSn());
		Order order = OperationalJudgment.judgment(orderService.getBySn(orderItem.getOrderSn()));
		afterSale.setStoreId(order.getStoreId());
		afterSale.setStoreName(order.getStoreName());

		// 写入订单商品信息
		afterSale.setGoodsImage(orderItem.getImage());
		afterSale.setGoodsName(orderItem.getGoodsName());
		afterSale.setSpecs(orderItem.getSpecs());
		afterSale.setFlowPrice(orderItem.getFlowPrice());

		// 写入交易流水号
		afterSale.setTradeSn(order.getTradeSn());
		afterSale.setOrderSn(order.getSn());
		afterSale.setPayOrderNo(order.getPayOrderNo());
		afterSale.setOrderItemSn(orderItem.getSn());

		// 写入状态
		afterSale.setServiceStatus(AfterSaleStatusEnum.APPLY.name());

		// TODO 退还积分

		// 创建售后单号
		afterSale.setSn(IdGeneratorUtils.createStr("A"));

		// 是否包含图片
		if (afterSaleAddCmd.images() != null) {
			afterSale.setAfterSaleImage(afterSaleAddCmd.images());
		}

		if (afterSale.getNum().equals(orderItem.getNum())) {
			// 计算退回金额
			afterSale.setApplyRefundPrice(orderItem.getFlowPrice());
		} else {
			// 单价计算
			BigDecimal utilPrice =
				CurrencyUtils.div(orderItem.getPriceDetailDTO().flowPrice(), orderItem.getNum());
			afterSale.setApplyRefundPrice(CurrencyUtils.mul(afterSale.getNum(), utilPrice));
		}

		// 添加售后
		this.save(afterSale);
		// 发送售后消息
		this.sendAfterSaleMessage(afterSale);

		// 根据售后状态。修改OrderItem订单中正在售后商品数量及状态
		this.updateOrderItemAfterSaleStatus(afterSale);

		return afterSale;
	}

	/**
	 * 修改OrderItem订单中正在售后的商品数量及OrderItem订单状态
	 */
	private void updateOrderItemAfterSaleStatus(AfterSalePO afterSale) {
		// 根据商品skuId及订单sn获取子订单
		OrderItem orderItem = orderItemService.getOne(new LambdaQueryWrapper<OrderItem>()
			.eq(OrderItem::getOrderSn, afterSale.getOrderSn())
			.eq(OrderItem::getSkuId, afterSale.getSkuId()));
		AfterSaleStatusEnum afterSaleStatusEnum = AfterSaleStatusEnum.valueOf(afterSale.getServiceStatus());

		switch (afterSaleStatusEnum) {
			// 判断当前售后的状态---申请中
			case APPLY -> orderItem.setReturnGoodsNumber(orderItem.getReturnGoodsNumber() + afterSale.getNum());
			// 判断当前售后的状态---已拒绝,买家取消售后,卖家终止售后
			case REFUSE, BUYER_CANCEL, SELLER_TERMINATION -> orderItem.setReturnGoodsNumber(
				orderItem.getReturnGoodsNumber() - afterSale.getNum());
			default -> {
			}
		}
		// 修改orderItem订单
		this.updateOrderItem(orderItem);
	}

	/**
	 * 检查当前订单状态是否为可申请当前售后类型的状态
	 *
	 * @param afterSaleAddCmd 售后
	 */
	private void checkAfterSaleType(AfterSaleAddCmd afterSaleAddCmd) {
		// 判断数据是否为空
		if (null == afterSaleAddCmd || CharSequenceUtil.isEmpty(afterSaleAddCmd.orderItemSn())) {
			throw new BusinessException(ResultEnum.ORDER_NOT_EXIST);
		}

		// 获取订单货物判断是否可申请售后
		OrderItem orderItem = orderItemService.getBySn(afterSaleAddCmd.orderItemSn());

		// 未申请售后或部分售后订单货物才能进行申请
		if (!orderItem.getAfterSaleStatus().equals(OrderItemAfterSaleStatusEnum.NOT_APPLIED.name())
			&& !orderItem.getAfterSaleStatus().equals(OrderItemAfterSaleStatusEnum.PART_AFTER_SALE.name())) {
			throw new BusinessException(ResultEnum.AFTER_SALES_BAN);
		}

		// 申请商品数量不能超过商品总数量-售后商品数量
		if (afterSaleAddCmd.num() > (orderItem.getNum() - orderItem.getReturnGoodsNumber())) {
			throw new BusinessException(ResultEnum.AFTER_GOODS_NUMBER_ERROR);
		}

		// 获取售后类型
		Order order = orderService.getBySn(orderItem.getOrderSn());
		AfterSaleTypeEnum afterSaleTypeEnum = AfterSaleTypeEnum.valueOf(afterSaleAddCmd.serviceType());
		switch (afterSaleTypeEnum) {
			case RETURN_MONEY -> {
				// 只处理已付款的售后
				if (!PayStatusEnum.PAID.name().equals(order.getPayStatus())) {
					throw new BusinessException(ResultEnum.AFTER_SALES_BAN);
				}
				this.checkAfterSaleReturnMoneyParam(afterSaleAddCmd);
			}
			case RETURN_GOODS -> {
				// 是否为有效状态
				boolean availableStatus = CharSequenceUtil.equalsAny(
					order.getOrderStatus(), OrderStatusEnum.DELIVERED.name(), OrderStatusEnum.COMPLETED.name());
				if (!PayStatusEnum.PAID.name().equals(order.getPayStatus()) && availableStatus) {
					throw new BusinessException(ResultEnum.AFTER_SALES_BAN);
				}
			}
			default -> {
			}
		}
	}

	/**
	 * 检测售后-退款参数
	 *
	 * @param afterSaleAddCmd 售后DTO
	 */
	private void checkAfterSaleReturnMoneyParam(AfterSaleAddCmd afterSaleAddCmd) {
		// 如果为线下支付银行信息不能为空
		if (AfterSaleRefundWayEnum.OFFLINE.name().equals(afterSaleAddCmd.refundWay())) {
			boolean emptyBankParam = CharSequenceUtil.isEmpty(afterSaleAddCmd.bankDepositName())
				|| CharSequenceUtil.isEmpty(afterSaleAddCmd.bankAccountName())
				|| CharSequenceUtil.isEmpty(afterSaleAddCmd.bankAccountNumber());
			if (emptyBankParam) {
				throw new BusinessException(ResultEnum.RETURN_MONEY_OFFLINE_BANK_ERROR);
			}
		}
	}

	/**
	 * 根据sn获取信息
	 *
	 * @param sn 订单sn
	 * @return 售后信息
	 */
	private AfterSalePO getBySn(String sn) {
		QueryWrapper<AfterSalePO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("sn", sn);
		return this.getOne(queryWrapper);
	}

	/**
	 * 根据售后编号修改售后单
	 *
	 * @param afterSaleSn 售后单号
	 * @param afterSale   售后单
	 */
	private void updateAfterSale(String afterSaleSn, AfterSalePO afterSale) {
		// 修改售后单状态
		LambdaUpdateWrapper<AfterSalePO> queryWrapper = Wrappers.lambdaUpdate();
		queryWrapper.eq(AfterSalePO::getSn, afterSaleSn);
		this.update(afterSale, queryWrapper);
	}

	/**
	 * 发送售后消息
	 *
	 * @param afterSale 售后对象
	 */
	private void sendAfterSaleMessage(AfterSalePO afterSale) {
		// 发送售后创建消息
		String destination =
			rocketmqCustomProperties.getAfterSaleTopic() + ":" + AfterSaleTagsEnum.AFTER_SALE_STATUS_CHANGE.name();
		// 发送订单变更mq消息
		rocketMQTemplate.asyncSend(
			destination, JSONUtil.toJsonStr(afterSale), RocketmqSendCallbackBuilder.commonCallback());
	}

	/**
	 * 功能描述: 获取售后商品数量及已完成售后商品数量修改orderItem订单
	 *
	 * @param orderItem,
	 * @param afterSaleList
	 */
	private void updateOrderItemGoodsNumber(OrderItem orderItem, List<AfterSalePO> afterSaleList) {
		// 根据售后状态获取不是已结束的售后记录
		List<AfterSalePO> implementList = afterSaleList.stream()
			.filter(afterSale -> afterSale.getServiceStatus().equals(AfterSaleStatusEnum.APPLY.name())
				|| afterSale.getServiceStatus().equals(AfterSaleStatusEnum.PASS.name())
				|| afterSale.getServiceStatus().equals(AfterSaleStatusEnum.BUYER_RETURN.name())
				|| afterSale.getServiceStatus().equals(AfterSaleStatusEnum.SELLER_CONFIRM.name())
				|| afterSale.getServiceStatus().equals(AfterSaleStatusEnum.WAIT_REFUND.name())
				|| afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name()))
			.toList();

		if (!implementList.isEmpty()) {
			// 遍历售后记录获取售后商品数量
			implementList.forEach(a -> orderItem.setReturnGoodsNumber(orderItem.getReturnGoodsNumber() + a.getNum()));
		}

		// 获取已完成售后订单数量
		List<AfterSalePO> completeList = afterSaleList.stream()
			.filter(afterSale -> afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name()))
			.toList();

		if (!completeList.isEmpty()) {
			// 遍历售后记录获取已完成售后商品数量
			completeList.forEach(a -> orderItem.setReturnGoodsNumber(orderItem.getReturnGoodsNumber() + a.getNum()));
		}
	}

	/**
	 * 功能描述: 修改orderItem订单
	 *
	 * @param orderItem 订单子项
	 */
	private void updateOrderItem(OrderItem orderItem) {
		// 订单状态不能为新订单,已失效订单或未申请订单才可以去修改订单信息
		OrderItemAfterSaleStatusEnum afterSaleTypeEnum =
			OrderItemAfterSaleStatusEnum.valueOf(orderItem.getAfterSaleStatus());
		switch (afterSaleTypeEnum) {
			// 售后状态为：未申请 部分售后 已申请
			case NOT_APPLIED, PART_AFTER_SALE, ALREADY_APPLIED -> {
				// 通过正在售后商品总数修改订单售后状态
				if (orderItem.getReturnGoodsNumber().equals(orderItem.getNum())) {
					// 修改订单的售后状态--已申请
					orderItem.setAfterSaleStatus(OrderItemAfterSaleStatusEnum.ALREADY_APPLIED.name());
				} else if (orderItem.getReturnGoodsNumber().equals(0)) {
					// 修改订单的售后状态--未申请
					orderItem.setAfterSaleStatus(OrderItemAfterSaleStatusEnum.NOT_APPLIED.name());
				} else {
					// 修改订单的售后状态--部分售后
					orderItem.setAfterSaleStatus(OrderItemAfterSaleStatusEnum.PART_AFTER_SALE.name());
				}
			}
			default -> {
			}
		}

		orderItemService.update(new LambdaUpdateWrapper<OrderItem>()
			.eq(OrderItem::getSn, orderItem.getSn())
			.set(OrderItem::getAfterSaleStatus, orderItem.getAfterSaleStatus())
			.set(OrderItem::getReturnGoodsNumber, orderItem.getReturnGoodsNumber()));
	}
}
