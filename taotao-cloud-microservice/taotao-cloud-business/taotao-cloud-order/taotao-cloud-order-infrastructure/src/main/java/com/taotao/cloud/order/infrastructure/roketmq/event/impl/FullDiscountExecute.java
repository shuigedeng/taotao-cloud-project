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

package com.taotao.cloud.order.infrastructure.roketmq.event.impl;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.enums.UserEnum;
import com.taotao.boot.common.utils.common.IdGeneratorUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.OrderTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.order.domain.cart.valueobject.CartVO;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderLogPO;
import com.taotao.cloud.order.infrastructure.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.roketmq.event.TradeEvent;
import java.util.ArrayList;
import java.util.List;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.easyes.common.enums.OrderTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单状态处理类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:35:00
 */
@Service
public class FullDiscountExecute implements TradeEvent, OrderStatusChangeEvent {

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private IFeignMemberApi memberApi;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IOrderItemService orderItemService;

	@Autowired
	private IOrderLogService orderLogService;

	@Autowired
	private IFeignMemberCouponApi memberCouponApi;

	@Autowired
	private IFeignGoodsSkuApi goodsSkuApi;

	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;

	@Autowired
	private RocketMQTemplate rocketMQTemplate;

	@Override
	public void orderCreate(TradeDTO tradeDTO) {
		tradeDTO.getCartList().forEach(cartVO -> {
			// 有满减优惠，则记录信息
			if ((cartVO.giftList() != null && !cartVO.giftList().isEmpty())
				|| (cartVO.giftPoint() != null && cartVO.giftPoint() > 0)
				|| (cartVO.giftCouponList() != null
				&& !cartVO.giftCouponList().isEmpty())) {
				redisRepository.set(CachePrefix.ORDER.getPrefix() + cartVO.sn(),
					JSONUtil.toJsonStr(cartVO));
			}
		});
	}

	@Override
	public void orderChange(OrderMessage orderMessage) {
		// 如果订单已支付
		if (orderMessage.newStatus().equals(OrderStatusEnum.PAID)) {
			LogUtils.info("满减活动，订单状态操作 {}",
				CachePrefix.ORDER.getPrefix() + orderMessage.orderSn());
			renderGift(
				JSONUtil.toBean(
					redisRepository
						.get(CachePrefix.ORDER.getPrefix() + orderMessage.getOrderSn())
						.toString(),
					CartVO.class),
				orderMessage);
		}
	}

	/**
	 * 渲染优惠券信息
	 *
	 * @param cartVO       购物车签证官
	 * @param orderMessage 订单消息
	 * @since 2022-05-16 17:35:11
	 */
	private void renderGift(CartVO cartVO, OrderMessage orderMessage) {
		// 没有优惠信息则跳过
		if (cartVO == null) {
			return;
		}

		Order order = orderService.getBySn(orderMessage.orderSn());
		// 赠送积分判定
		try {
			if (cartVO.giftPoint() != null && cartVO.giftPoint() > 0) {
				memberApi.updateMemberPoint(
					cartVO.giftPoint().longValue(),
					PointTypeEnum.INCREASE.name(),
					order.getMemberId(),
					"订单满优惠赠送积分" + cartVO.giftPoint());
			}
		}
		catch (Exception e) {
			LogUtils.error("订单赠送积分异常", e);
		}

		try {
			// 优惠券判定
			if (cartVO.giftCouponList() != null && !cartVO.giftCouponList().isEmpty()) {
				cartVO.giftCouponList()
					.forEach(couponId ->
						memberCouponApi.receiveCoupon(couponId, order.getMemberId(),
							order.getMemberName()));
			}
		}
		catch (Exception e) {
			LogUtils.error("订单赠送优惠券异常", e);
		}

		try {
			// 赠品潘迪ing
			if (cartVO.giftList() != null && !cartVO.giftList().isEmpty()) {
				generatorGiftOrder(cartVO.giftList(), order);
			}
		}
		catch (Exception e) {
			LogUtils.error("订单赠送赠品异常", e);
		}
	}

	/**
	 * 生成赠品订单
	 *
	 * @param skuIds      赠品sku信息
	 * @param originOrder 赠品原订单信息
	 * @since 2022-05-16 17:35:15
	 */
	private void generatorGiftOrder(List<String> skuIds, Order originOrder) {
		// 获取赠品列表
		List<GoodsSkuSpecGalleryVO> goodsSkus = goodsSkuApi.getGoodsSkuByIdFromCache(skuIds);

		// 赠品判定
		if (goodsSkus == null || goodsSkus.isEmpty()) {
			LogUtils.error("赠品不存在：{}", skuIds);
			return;
		}

		// 赠品分类，分为实体商品/虚拟商品/电子卡券
		List<GoodsSkuSpecGalleryVO> physicalSkus = goodsSkus.stream()
			.filter(goodsSku -> goodsSku.goodsSkuBase().goodsType()
				.equals(GoodsTypeEnum.PHYSICAL_GOODS.name()))
			.toList();
		List<GoodsSkuSpecGalleryVO> virtualSkus = goodsSkus.stream()
			.filter(goodsSku -> goodsSku.goodsSkuBase().goodsType()
				.equals(GoodsTypeEnum.VIRTUAL_GOODS.name()))
			.toList();
		List<GoodsSkuSpecGalleryVO> eCouponSkus = goodsSkus.stream()
			.filter(goodsSku -> goodsSku.goodsSkuBase().goodsType()
				.equals(GoodsTypeEnum.E_COUPON.name()))
			.toList();

		// 如果赠品不为空，则生成对应的赠品订单
		if (!physicalSkus.isEmpty()) {
			giftOrderHandler(physicalSkus, originOrder, OrderTypeEnum.NORMAL);
		}
		if (!virtualSkus.isEmpty()) {
			giftOrderHandler(virtualSkus, originOrder, OrderTypeEnum.VIRTUAL);
		}
		if (!eCouponSkus.isEmpty()) {
			giftOrderHandler(eCouponSkus, originOrder, OrderTypeEnum.E_COUPON);
		}
	}

	/**
	 * 赠品订单处理
	 *
	 * @param skuList       赠品列表
	 * @param originOrder   原始订单
	 * @param orderTypeEnum 订单类型
	 * @since 2022-05-16 17:35:18
	 */
	private void giftOrderHandler(List<GoodsSkuSpecGalleryVO> skuList, Order originOrder,
		OrderTypeEnum orderTypeEnum) {
		// 初始化订单对象/订单日志/自订单
		Order order = new Order();
		List<OrderItem> orderItems = new ArrayList<>();
		List<OrderLogPO> orderLogPOS = new ArrayList<>();
		// 初始化价格详情
		PriceDetailDTO priceDetailDTO = new PriceDetailDTO();
		// 复制通用属性
		BeanUtil.copyProperties(originOrder, order, "id");
		BeanUtil.copyProperties(priceDetailDTO, order, "id");
		// 生成订单参数
		order.setSn(IdGeneratorUtils.createStr("G"));
		order.setOrderPromotionType(OrderPromotionTypeEnum.GIFT.name());
		order.setOrderStatus(OrderStatusEnum.UNPAID.name());
		order.setPayStatus(PayStatusEnum.PAID.name());
		order.setOrderType(orderTypeEnum.name());
		order.setNeedReceipt(false);
		order.setPriceDetailDTO(priceDetailDTO);
		order.setClientType(originOrder.getClientType());
		// 订单日志
		String message = "赠品订单[" + order.getSn() + "]创建";
		orderLogPOS.add(new OrderLogPO(
			order.getSn(),
			originOrder.getMemberId(),
			UserEnum.MEMBER.name(),
			originOrder.getMemberName(),
			message));

		// 生成子订单
		for (GoodsSkuSpecGalleryVO goodsSku : skuList) {
			OrderItem orderItem = new OrderItem();
			BeanUtil.copyProperties(goodsSku, orderItem, "id");
			BeanUtil.copyProperties(priceDetailDTO, orderItem, "id");
			orderItem.setAfterSaleStatus(OrderItemAfterSaleStatusEnum.NEW.name());
			orderItem.setCommentStatus(CommentStatusEnum.NEW.name());
			orderItem.setComplainStatus(OrderComplaintStatusEnum.NEW.name());
			orderItem.setNum(1);
			orderItem.setOrderSn(order.getSn());
			orderItem.setImage(goodsSku.goodsSkuBase().thumbnail());
			orderItem.setGoodsName(goodsSku.goodsSkuBase().goodsName());
			orderItem.setSkuId(goodsSku.goodsSkuBase().id());
			orderItem.setCategoryId(Long.valueOf(goodsSku.goodsSkuBase()
				.categoryPath()
				.substring(goodsSku.goodsSkuBase().categoryPath().lastIndexOf(",") + 1)));
			orderItem.setGoodsPrice(goodsSku.goodsSkuBase().price());
			orderItem.setPriceDetailDTO(priceDetailDTO);
			orderItems.add(orderItem);
		}

		// 保存订单
		orderService.save(order);
		orderItemService.saveBatch(orderItems);
		orderLogService.saveBatch(orderLogPOS);

		// 发送订单已付款消息（PS:不在这里处理逻辑是因为期望加交给消费者统一处理库存等等问题）
		OrderMessage orderMessage = OrderMessageBuilder.builder()
			.orderSn(order.getSn())
			.paymentMethod(order.getPaymentMethod())
			.newStatus(OrderStatusEnum.PAID)
			.build();

		String destination =
			rocketmqCustomProperties.getOrderTopic() + ":" + OrderTagsEnum.STATUS_CHANGE.name();
		// 发送订单变更mq消息
		rocketMQTemplate.asyncSend(
			destination, JSONUtil.toJsonStr(orderMessage),
			RocketmqSendCallbackBuilder.commonCallback());
	}
}
