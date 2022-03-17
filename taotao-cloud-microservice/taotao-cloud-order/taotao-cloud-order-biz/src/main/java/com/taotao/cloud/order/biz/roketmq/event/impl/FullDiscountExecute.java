package com.taotao.cloud.order.biz.roketmq.event.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.enums.UserEnums;
import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.dto.order.OrderMessage;
import com.taotao.cloud.order.api.dto.order.PriceDetailDTO;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.api.vo.cart.CartVO;
import com.taotao.cloud.order.biz.entity.order.Order;
import com.taotao.cloud.order.biz.entity.trade.OrderLog;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.biz.roketmq.event.TradeEvent;
import com.taotao.cloud.order.biz.service.order.OrderItemService;
import com.taotao.cloud.order.biz.service.order.OrderService;
import com.taotao.cloud.order.biz.service.trade.OrderLogService;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.OrderTagsEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单状态处理类
 **/
@Service
public class FullDiscountExecute implements TradeEvent, OrderStatusChangeEvent {


    @Autowired
    private Cache cache;


    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private GoodsSkuService goodsSkuService;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void orderCreate(TradeDTO tradeDTO) {
        tradeDTO.getCartList().forEach(
                cartVO -> {
                    //有满减优惠，则记录信息
                    if ((cartVO.getGiftList() != null && !cartVO.getGiftList().isEmpty())
                            || (cartVO.getGiftPoint() != null && cartVO.getGiftPoint() > 0)
                            || (cartVO.getGiftCouponList() != null && !cartVO.getGiftCouponList().isEmpty())) {
                        cache.put(CachePrefix.ORDER.getPrefix() + cartVO.getSn(), JSONUtil.toJsonStr(cartVO));
                    }
                }
        );
    }

    @Override
    public void orderChange(OrderMessage orderMessage) {
        //如果订单已支付
        if (orderMessage.getNewStatus().equals(OrderStatusEnum.PAID)) {
            log.debug("满减活动，订单状态操作 {}", CachePrefix.ORDER.getPrefix() + orderMessage.getOrderSn());
            renderGift(JSONUtil.toBean(cache.getString(CachePrefix.ORDER.getPrefix() + orderMessage.getOrderSn()), CartVO.class), orderMessage);
        }
    }

    /**
     * 渲染优惠券信息
     */
    private void renderGift(CartVO cartVO, OrderMessage orderMessage) {
        //没有优惠信息则跳过
        if (cartVO == null) {
            return;
        }
        Order order = orderService.getBySn(orderMessage.getOrderSn());
        //赠送积分判定
        try {
            if (cartVO.getGiftPoint() != null && cartVO.getGiftPoint() > 0) {
                memberService.updateMemberPoint(cartVO.getGiftPoint().longValue(), PointTypeEnum.INCREASE.name(),
                        order.getMemberId(), "订单满优惠赠送积分" + cartVO.getGiftPoint());
            }
        } catch (Exception e) {
            log.error("订单赠送积分异常", e);
        }


        try {
            //优惠券判定
            if (cartVO.getGiftCouponList() != null && !cartVO.getGiftCouponList().isEmpty()) {
                cartVO.getGiftCouponList().forEach(couponId -> memberCouponService.receiveCoupon(couponId, order.getMemberId(), order.getMemberName()));
            }
        } catch (Exception e) {
            log.error("订单赠送优惠券异常", e);
        }

        try {
            //赠品潘迪ing
            if (cartVO.getGiftList() != null && !cartVO.getGiftList().isEmpty()) {
                generatorGiftOrder(cartVO.getGiftList(), order);
            }
        } catch (Exception e) {
            log.error("订单赠送赠品异常", e);
        }
    }

    /**
     * 生成赠品订单
     *
     * @param skuIds      赠品sku信息
     * @param originOrder 赠品原订单信息
     */
    private void generatorGiftOrder(List<String> skuIds, Order originOrder) {
        //获取赠品列表
        List<GoodsSku> goodsSkus = goodsSkuService.getGoodsSkuByIdFromCache(skuIds);

        //赠品判定
        if (goodsSkus == null || goodsSkus.isEmpty()) {
            log.error("赠品不存在：{}", skuIds);
            return;
        }

        //赠品分类，分为实体商品/虚拟商品/电子卡券
        List<GoodsSku> physicalSkus = goodsSkus.stream().filter(goodsSku -> goodsSku.getGoodsType().equals(GoodsTypeEnum.PHYSICAL_GOODS.name())).collect(Collectors.toList());
        List<GoodsSku> virtualSkus = goodsSkus.stream().filter(goodsSku -> goodsSku.getGoodsType().equals(GoodsTypeEnum.VIRTUAL_GOODS.name())).collect(Collectors.toList());
        List<GoodsSku> eCouponSkus = goodsSkus.stream().filter(goodsSku -> goodsSku.getGoodsType().equals(GoodsTypeEnum.E_COUPON.name())).collect(Collectors.toList());

        //如果赠品不为空，则生成对应的赠品订单
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
     */
    private void giftOrderHandler(List<GoodsSku> skuList, Order originOrder, OrderTypeEnum orderTypeEnum) {
        //初始化订单对象/订单日志/自订单
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderLog> orderLogs = new ArrayList<>();
        //初始化价格详情
        PriceDetailDTO priceDetailDTO = new PriceDetailDTO();
        //复制通用属性
        BeanUtil.copyProperties(originOrder, order, "id");
        BeanUtil.copyProperties(priceDetailDTO, order, "id");
        //生成订单参数
        order.setSn(SnowFlake.createStr("G"));
        order.setOrderPromotionType(OrderPromotionTypeEnum.GIFT.name());
        order.setOrderStatus(OrderStatusEnum.UNPAID.name());
        order.setPayStatus(PayStatusEnum.PAID.name());
        order.setOrderType(orderTypeEnum.name());
        order.setNeedReceipt(false);
        order.setPriceDetailDTO(priceDetailDTO);
        order.setClientType(originOrder.getClientType());
        //订单日志
        String message = "赠品订单[" + order.getSn() + "]创建";
        orderLogs.add(new OrderLog(order.getSn(), originOrder.getMemberId(), UserEnums.MEMBER.name(), originOrder.getMemberName(), message));

        //生成子订单
        for (GoodsSku goodsSku : skuList) {
            OrderItem orderItem = new OrderItem();
            BeanUtil.copyProperties(goodsSku, orderItem, "id");
            BeanUtil.copyProperties(priceDetailDTO, orderItem, "id");
            orderItem.setAfterSaleStatus(OrderItemAfterSaleStatusEnum.NEW.name());
            orderItem.setCommentStatus(CommentStatusEnum.NEW.name());
            orderItem.setComplainStatus(OrderComplaintStatusEnum.NEW.name());
            orderItem.setNum(1);
            orderItem.setOrderSn(order.getSn());
            orderItem.setImage(goodsSku.getThumbnail());
            orderItem.setGoodsName(goodsSku.getGoodsName());
            orderItem.setSkuId(goodsSku.getId());
            orderItem.setCategoryId(goodsSku.getCategoryPath().substring(
                    goodsSku.getCategoryPath().lastIndexOf(",") + 1
            ));
            orderItem.setGoodsPrice(goodsSku.getPrice());
            orderItem.setPriceDetailDTO(priceDetailDTO);
            orderItems.add(orderItem);
        }
        //保存订单
        orderService.save(order);
        orderItemService.saveBatch(orderItems);
        orderLogService.saveBatch(orderLogs);


        //发送订单已付款消息（PS:不在这里处理逻辑是因为期望加交给消费者统一处理库存等等问题）
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderSn(order.getSn());
        orderMessage.setPaymentMethod(order.getPaymentMethod());
        orderMessage.setNewStatus(OrderStatusEnum.PAID);

        String destination = rocketmqCustomProperties.getOrderTopic() + ":" + OrderTagsEnum.STATUS_CHANGE.name();
        //发送订单变更mq消息
        rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(orderMessage), RocketmqSendCallbackBuilder.commonCallback());
    }
}
