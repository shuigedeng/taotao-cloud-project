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

package com.taotao.cloud.order.infrastructure.persistent.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.common.enums.ClientTypeEnum;
import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import com.taotao.cloud.order.api.enums.cart.DeliveryMethodEnum;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.sys.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.sys.model.dto.order.PriceDetailDTO;
import com.taotao.cloud.order.sys.model.vo.cart.CartVO;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 订单表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:19
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = OrderPO.TABLE_NAME)
@TableName(OrderPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderPO.TABLE_NAME, comment = "订单表")
public class OrderPO extends BaseSuperEntity<OrderPO, Long> {

    public static final String TABLE_NAME = "tt_order";

    /** 订单编号 */
    @Column(name = "sn", columnDefinition = "varchar(64) not null comment '订单编号'")
    private String sn;

    /** 交易编号 关联Trade */
    @Column(name = "trade_sn", columnDefinition = "varchar(64) not null comment '交易编号 关联Trade'")
    private String tradeSn;

    /** 店铺ID */
    @Column(name = "store_id", columnDefinition = "bigint not null comment '店铺ID'")
    private Long storeId;

    /** 店铺名称 */
    @Column(name = "store_name", columnDefinition = "varchar(64) not null comment '店铺名称'")
    private String storeName;

    /** 会员ID */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long memberId;

    /** 用户名 */
    @Column(name = "member_name", columnDefinition = "varchar(64) not null comment '用户名'")
    private String memberName;

    /**
     * 订单状态
     *
     * @see OrderStatusEnum
     */
    @Column(name = "order_status", columnDefinition = "varchar(64) not null comment '订单状态'")
    private String orderStatus;

    /**
     * 付款状态
     *
     * @see PayStatusEnum
     */
    @Column(name = "pay_status", columnDefinition = "varchar(64) not null comment '付款状态'")
    private String payStatus;

    /**
     * 货运状态
     *
     * @see DeliverStatusEnum
     */
    @Column(name = "deliver_status", columnDefinition = "varchar(64) not null comment '货运状态'")
    private String deliverStatus;

    /** 第三方付款流水号 */
    @Column(name = "receivable_no", columnDefinition = "varchar(255) not null comment '第三方付款流水号'")
    private String receivableNo;

    /** 支付方式 */
    @Column(name = "payment_method", columnDefinition = "varchar(64) not null comment '支付方式'")
    private String paymentMethod;

    /** 支付时间 */
    @Column(name = "member_id", columnDefinition = "datetime null comment '支付时间'")
    private LocalDateTime paymentTime;

    /** 收件人姓名 */
    @Column(name = "consignee_name", columnDefinition = "varchar(64) not null comment '收件人姓名'")
    private String consigneeName;

    /** 收件人手机 */
    @Column(name = "consignee_mobile", columnDefinition = "varchar(64) not null comment '收件人手机'")
    private String consigneeMobile;

    /**
     * 配送方式
     *
     * @see DeliveryMethodEnum
     */
    @Column(name = "delivery_method", columnDefinition = "varchar(64) not null comment '配送方式'")
    private String deliveryMethod;

    /** 地址名称， ','分割 */
    @Column(name = "consignee_address_path", columnDefinition = "varchar(64) not null comment '地址名称，逗号分割'")
    private String consigneeAddressPath;

    /** 地址id，','分割 */
    @Column(name = "consignee_address_id_path", columnDefinition = "varchar(64) not null comment '地址id，逗号分割'")
    private String consigneeAddressIdPath;

    /** 详细地址 */
    @Column(name = "consignee_detail", columnDefinition = "varchar(1024) not null comment '详细地址'")
    private String consigneeDetail;

    /** 总价格 */
    @Column(name = "flow_price", columnDefinition = "decimal(10,2) not null comment '总价格'")
    private BigDecimal flowPrice;

    /** 商品价格 */
    @Column(name = "goods_price", columnDefinition = "decimal(10,2) not null comment '商品价格'")
    private BigDecimal goodsPrice;

    /** 运费 */
    @Column(name = "freight_price", columnDefinition = "decimal(10,2) not null comment '运费'")
    private BigDecimal freightPrice;

    /** 优惠的金额 */
    @Column(name = "discount_price", columnDefinition = "decimal(10,2) not null comment '优惠的金额'")
    private BigDecimal discountPrice;

    /** 修改价格 */
    @Column(name = "update_price", columnDefinition = "decimal(10,2) not null comment '修改价格'")
    private BigDecimal updatePrice;

    /** 发货单号 */
    @Column(name = "logistics_no", columnDefinition = "varchar(64) not null comment '发货单号'")
    private String logisticsNo;

    /** 物流公司CODE */
    @Column(name = "logistics_code", columnDefinition = "varchar(64) not null comment '物流公司CODE'")
    private String logisticsCode;

    /** 物流公司名称 */
    @Column(name = "logistics_name", columnDefinition = "varchar(255) not null comment '物流公司名称'")
    private String logisticsName;

    /** 订单商品总重量 */
    @Column(name = "weight", columnDefinition = "decimal(10,2) not null comment '订单商品总重量'")
    private BigDecimal weight;

    /** 商品数量 */
    @Column(name = "goods_num", columnDefinition = "int not null comment '商品数量'")
    private Integer goodsNum;

    /** 买家订单备注 */
    @Column(name = "remark", columnDefinition = "text not null comment '买家订单备注'")
    private String remark;

    /** 订单取消原因 */
    @Column(name = "cancel_reason", columnDefinition = "varchar(255) not null comment '订单取消原因'")
    private String cancelReason;

    /** 完成时间 */
    @Column(name = "complete_time", columnDefinition = "datetime null comment '完成时间'")
    private LocalDateTime completeTime;

    /** 送货时间 */
    @Column(name = "logistics_time", columnDefinition = "datetime null comment '送货时间'")
    private LocalDateTime logisticsTime;

    /** 支付方式返回的交易号 */
    @Column(name = "pay_order_no", columnDefinition = "varchar(64) not null comment '支付方式返回的交易号'")
    private String payOrderNo;

    /**
     * 订单来源
     *
     * @see ClientTypeEnum
     */
    @Column(name = "client_type", columnDefinition = "varchar(64) null comment '订单来源'")
    private String clientType;

    /** 是否需要发票 */
    @Column(name = "need_receipt", columnDefinition = "boolean not null comment '是否需要发票'")
    private Boolean needReceipt;

    /** 是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空 */
    @Column(name = "parent_order_sn", columnDefinition = "varchar(64) not null comment '是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空'")
    private String parentOrderSn;

    /** 是否为某订单类型的订单，如果是则为订单类型的id，否则为空 */
    @Column(name = "promotion_id", columnDefinition = "bigint  null comment '是否为某订单类型的订单，如果是则为订单类型的id，否则为空'")
    private Long promotionId;

    /**
     * 订单类型
     *
     * @see OrderTypeEnum
     */
    @Column(name = "order_type", columnDefinition = "varchar(64) not null comment '订单类型'")
    private String orderType;

    /**
     * 订单促销类型
     *
     * @see OrderPromotionTypeEnum
     */
    @Column(name = "order_promotion_type", columnDefinition = "varchar(64) not null comment '订单促销类型'")
    private String orderPromotionType;

    /** 价格详情 */
    @Column(name = "price_detail", columnDefinition = "varchar(64) not null comment '价格详情'")
    private String priceDetail;

    /** 订单是否支持原路退回 */
    @Column(name = "can_return", columnDefinition = "boolean not null comment '订单是否支持原路退回'")
    private Boolean canReturn;

    /** 提货码 */
    @Column(name = "verification_code", columnDefinition = "varchar(64) not null comment '提货码'")
    private String verificationCode;

    /** 分销员ID */
    @Column(name = "distribution_id", columnDefinition = "bigint not null comment '分销员ID'")
    private Long distributionId;

    /** 使用的店铺会员优惠券id(,区分) */
    @Column(
            name = "use_store_member_coupon_ids",
            columnDefinition = "varchar(255) not null comment '使用的店铺会员优惠券id(,区分)'")
    private String useStoreMemberCouponIds;

    /** 使用的平台会员优惠券id */
    @Column(name = "use_platform_member_coupon_id", columnDefinition = "varchar(64) not null comment '使用的平台会员优惠券id'")
    private String usePlatformMemberCouponId;

    /**
     * 构建订单
     *
     * @param cartVO 购物车VO
     * @param tradeDTO 交易DTO
     */
    public OrderPO(CartVO cartVO, TradeDTO tradeDTO) {
        Long oldId = this.getId();
        BeanUtils.copyProperties(tradeDTO, this);
        BeanUtils.copyProperties(cartVO.getPriceDetailDTO(), this);
        BeanUtils.copyProperties(cartVO, this);
        // 填写订单类型
        this.setTradeType(cartVO, tradeDTO);
        setId(oldId);

        // 设置默认支付状态
        this.setOrderStatus(OrderStatusEnum.UNPAID.name());
        this.setPayStatus(PayStatusEnum.UNPAID.name());
        this.setDeliverStatus(DeliverStatusEnum.UNDELIVERED.name());
        this.setTradeSn(tradeDTO.getSn());
        this.setRemark(cartVO.getRemark());
        this.setFreightPrice(tradeDTO.getPriceDetailDTO().getFreightPrice());
        // 会员收件信息
        this.setConsigneeAddressIdPath(tradeDTO.getMemberAddress().getConsigneeAddressIdPath());
        this.setConsigneeAddressPath(tradeDTO.getMemberAddress().getConsigneeAddressPath());
        this.setConsigneeDetail(tradeDTO.getMemberAddress().getDetail());
        this.setConsigneeMobile(tradeDTO.getMemberAddress().getMobile());
        this.setConsigneeName(tradeDTO.getMemberAddress().getName());
        // 平台优惠券判定
        if (tradeDTO.getPlatformCoupon() != null) {
            this.setUsePlatformMemberCouponId(
                    tradeDTO.getPlatformCoupon().getMemberCoupon().getId());
        }
        // 店铺优惠券判定
        if (tradeDTO.getStoreCoupons() != null && !tradeDTO.getStoreCoupons().isEmpty()) {
            StringBuilder storeCouponIds = new StringBuilder();
            for (String s : tradeDTO.getStoreCoupons().keySet()) {
                storeCouponIds.append(s).append(",");
            }
            this.setUseStoreMemberCouponIds(storeCouponIds.toString());
        }
    }

    /**
     * 填写交易（订单）类型 1.判断是普通、促销订单 2.普通订单进行区分：实物订单、虚拟订单 3.促销订单判断货物进行区分实物、虚拟商品。 4.拼团订单需要填写父订单ID
     *
     * @param cartVO 购物车VO
     * @param tradeDTO 交易DTO
     */
    private void setTradeType(CartVO cartVO, TradeDTO tradeDTO) {
        // 判断是否为普通订单、促销订单
        if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.CART)
                || tradeDTO.getCartTypeEnum().equals(CartTypeEnum.BUY_NOW)) {
            this.setOrderType(OrderTypeEnum.NORMAL.name());
        } else if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.VIRTUAL)) {
            this.setOrderType(OrderTypeEnum.VIRTUAL.name());
        } else {
            // 促销订单（拼团、积分）-判断购买的是虚拟商品还是实物商品
            String goodsType = cartVO.getCheckedSkuList().get(0).getGoodsSku().getGoodsType();
            if (StrUtil.isEmpty(goodsType) || goodsType.equals(GoodsTypeEnum.PHYSICAL_GOODS.name())) {
                this.setOrderType(OrderTypeEnum.NORMAL.name());
            } else {
                this.setOrderType(OrderTypeEnum.VIRTUAL.name());
            }
            // 填写订单的促销类型
            this.setOrderPromotionType(tradeDTO.getCartTypeEnum().name());

            // 判断是否为拼团订单，如果为拼团订单获取拼团ID，判断是否为主订单
            if (tradeDTO.getCartTypeEnum().name().equals(PromotionTypeEnum.PINTUAN.name())) {
                Optional<String> pintuanId = cartVO.getCheckedSkuList().get(0).getPromotions().stream()
                        .filter(i -> i.getPromotionType().equals(PromotionTypeEnum.PINTUAN.name()))
                        .map(PromotionGoods::getPromotionId)
                        .findFirst();
                promotionId = pintuanId.get();
            }
        }
    }

    public PriceDetailDTO getPriceDetailDTO() {
        try {
            return JSONUtil.toBean(priceDetail, PriceDetailDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void setPriceDetailDTO(PriceDetailDTO priceDetail) {
        this.priceDetail = JSONUtil.toJsonStr(priceDetail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderPO orderPO = (OrderPO) o;
        return getId() != null && Objects.equals(getId(), orderPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
