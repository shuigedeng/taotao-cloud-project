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
import com.taotao.cloud.order.api.enums.order.FlowTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 商家订单流水表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:02:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = StoreFlowPO.TABLE_NAME)
@TableName(StoreFlowPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = StoreFlowPO.TABLE_NAME, comment = "商家订单流水表")
public class StoreFlowPO extends BaseSuperEntity<StoreFlowPO, Long> {

    public static final String TABLE_NAME = "tt_store_flow";

    @Serial
    private static final long serialVersionUID = -5998757398902747939L;
    /** 流水编号 */
    @Column(name = "sn", columnDefinition = "varchar(64) not null comment '流水编号'")
    private String sn;
    /** 订单sn */
    @Column(name = "order_sn", columnDefinition = "varchar(64) not null comment '订单sn'")
    private String orderSn;
    /** 子订单sn */
    @Column(name = "order_item_sn", columnDefinition = "varchar(64) not null comment '子订单sn'")
    private String orderItemSn;
    /** 售后SN */
    @Column(name = "refund_sn", columnDefinition = "varchar(64) not null comment '售后SN'")
    private String refundSn;
    /** 店铺id */
    @Column(name = "store_id", columnDefinition = "varchar(64) not null comment '店铺id'")
    private Long storeId;
    /** 店铺名称 */
    @Column(name = "store_name", columnDefinition = "varchar(64) not null comment '店铺名称'")
    private String storeName;
    /** 会员id */
    @Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员id'")
    private Long memberId;
    /** 会员名称 */
    @Column(name = "membe_name", columnDefinition = "varchar(64) not null comment '会员名称'")
    private String memberName;
    /** 商品ID */
    @Column(name = "goods_id", columnDefinition = "varchar(64) not null comment '商品ID'")
    private Long goodsId;
    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(64) not null comment '商品名称'")
    private String goodsName;
    /** 货品ID */
    @Column(name = "sku_id", columnDefinition = "varchar(64) not null comment '货品ID'")
    private Long skuId;
    /** 图片 */
    @Column(name = "image", columnDefinition = "varchar(64) not null comment '图片'")
    private String image;
    /** 分类ID */
    @Column(name = "category_id", columnDefinition = "varchar(64) not null comment '分类ID'")
    private Long categoryId;
    /** 规格json */
    @Column(name = "specs", columnDefinition = "json not null comment '规格json'")
    private String specs;
    /**
     * 流水类型：PAY/REFUND 支付/退款
     *
     * @see FlowTypeEnum
     */
    @Column(name = "flow_type", columnDefinition = "varchar(64) not null comment '流水类型：PAY/REFUND 支付/退款'")
    private String flowType;

    /**
     * 订单促销类型
     *
     * @see OrderPromotionTypeEnum
     */
    @Column(name = "order_promotion_type", columnDefinition = "varchar(64) not null comment '订单促销类型'")
    private String orderPromotionType;
    /** 积分活动商品结算价格 */
    @Column(name = "point_settlement_price", columnDefinition = "varchar(64) not null comment '积分活动商品结算价格'")
    private BigDecimal pointSettlementPrice;
    /** 砍价活动商品结算价格 */
    @Column(name = "kanjia_settlement_price", columnDefinition = "varchar(64) not null comment '砍价活动商品结算价格'")
    private BigDecimal kanjiaSettlementPrice;
    /** 平台优惠券 使用金额 */
    @Column(name = "site_coupon_price", columnDefinition = "varchar(64) not null comment '平台优惠券 使用金额'")
    private BigDecimal siteCouponPrice;
    /** 站点优惠券佣金比例 */
    @Column(name = "site_coupon_point", columnDefinition = "varchar(64) not null comment '站点优惠券佣金比例'")
    private BigDecimal siteCouponPoint;
    /** 站点优惠券佣金 */
    @Column(name = "site_coupon_commission", columnDefinition = "varchar(64) not null comment '站点优惠券佣金'")
    private BigDecimal siteCouponCommission;
    /** 单品分销返现支出 */
    @Column(name = "distribution_rebate", columnDefinition = "varchar(64) not null comment '单品分销返现支出'")
    private BigDecimal distributionRebate;
    /** 平台收取交易佣金 */
    @Column(name = "commission_price", columnDefinition = "varchar(64) not null comment '平台收取交易佣金'")
    private BigDecimal commissionPrice;
    /** 流水金额 */
    @Column(name = "final_price", columnDefinition = "varchar(64) not null comment '流水金额'")
    private BigDecimal finalPrice;
    /** 最终结算金额 */
    @Column(name = "bill_price", columnDefinition = "varchar(64) not null comment '最终结算金额'")
    private BigDecimal billPrice;
    /** 第三方交易流水号 */
    @Column(name = "transaction_id", columnDefinition = "varchar(64) not null comment '第三方交易流水号'")
    private String transactionId;
    /** 支付方式名称 */
    @Column(name = "payment_name", columnDefinition = "varchar(64) not null comment '支付方式名称'")
    private String paymentName;
    /** 销售量 */
    @Column(name = "num", columnDefinition = "varchar(64) not null comment '销售量'")
    private Integer num;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StoreFlowPO storeFlowPO = (StoreFlowPO) o;
        return getId() != null && Objects.equals(getId(), storeFlowPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
