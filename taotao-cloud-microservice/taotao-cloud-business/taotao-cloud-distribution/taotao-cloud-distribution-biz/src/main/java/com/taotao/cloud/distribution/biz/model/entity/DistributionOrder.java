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

package com.taotao.cloud.distribution.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.distribution.api.enums.DistributionOrderStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 分销订单
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 14:59:13
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DistributionOrder.TABLE_NAME)
@TableName(DistributionOrder.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = DistributionOrder.TABLE_NAME, comment = "分销订单表")
public class DistributionOrder extends BaseSuperEntity<DistributionOrder, Long> {

    public static final String TABLE_NAME = "tt_distribution_order";

    /**
     * 分销订单状态
     *
     * @see DistributionOrderStatusEnum
     */
    @Column(name = "distribution_order_status", columnDefinition = "varchar(255) not null  comment '分销订单状态'")
    private String distributionOrderStatus;
    /** 购买会员的id */
    @Column(name = "member_id", columnDefinition = "bigint not null  comment '购买会员的id'")
    private Long memberId;
    /** 购买会员的名称 */
    @Column(name = "member_name", columnDefinition = "varchar(255) not null  comment '购买会员的名称'")
    private String memberName;
    /** 分销员id */
    @Column(name = "distribution_id", columnDefinition = "bigint not null  comment '分销员id'")
    private Long distributionId;
    /** 分销员名称 */
    @Column(name = "distribution_name", columnDefinition = "varchar(255) not null  comment '分销员名称'")
    private String distributionName;
    /** 解冻日期 */
    @Column(name = "settle_cycle", columnDefinition = "datetime not null  comment '解冻日期'")
    private LocalDateTime settleCycle;
    /** 提成金额 */
    @Column(name = "rebate", columnDefinition = "decimal(10,2) not null  comment '提成金额'")
    private BigDecimal rebate;
    /** 退款金额 */
    @Column(name = "sell_back_rebate", columnDefinition = "decimal(10,2) not null  comment '退款金额'")
    private BigDecimal sellBackRebate;
    /** 店铺id */
    @Column(name = "store_id", columnDefinition = "bigint not null  comment '店铺id'")
    private Long storeId;
    /** 店铺名称 */
    @Column(name = "store_name", columnDefinition = "varchar(255) not null  comment '店铺名称'")
    private String storeName;
    /** 订单编号 */
    @Column(name = "order_sn", columnDefinition = "varchar(255) not null  comment '订单编号'")
    private String orderSn;
    /** 子订单编号 */
    @Column(name = "order_item_sn", columnDefinition = "varchar(255) not null  comment '子订单编号'")
    private String orderItemSn;
    /** 商品ID */
    @Column(name = "goods_id", columnDefinition = "bigint not null  comment '商品ID'")
    private Long goodsId;
    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(255) not null  comment '商品名称'")
    private String goodsName;
    /** 货品ID */
    @Column(name = "sku_id", columnDefinition = "bigint not null  comment '货品ID'")
    private Long skuId;
    /** 规格 */
    @Column(name = "specs", columnDefinition = "varchar(255) not null  comment '规格'")
    private String specs;
    /** 图片 */
    @Column(name = "image", columnDefinition = "varchar(255) not null  comment '图片'")
    private String image;
    /** 商品数量 */
    @Column(name = "num", columnDefinition = "int not null  comment '商品数量'")
    private Integer num;

    public DistributionOrder(StoreFlow storeFlow) {
        distributionOrderStatus = DistributionOrderStatusEnum.WAIT_BILL.name();
        memberId = storeFlow.getMemberId();
        memberName = storeFlow.getMemberName();
        rebate = storeFlow.getDistributionRebate();
        storeId = storeFlow.getStoreId();
        storeName = storeFlow.getStoreName();
        orderSn = storeFlow.getOrderSn();
        orderItemSn = storeFlow.getOrderItemSn();
        goodsId = storeFlow.getGoodsId();
        goodsName = storeFlow.getGoodsName();
        skuId = storeFlow.getSkuId();
        specs = storeFlow.getSpecs();
        image = storeFlow.getImage();
        num = storeFlow.getNum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DistributionOrder distributionOrder = (DistributionOrder) o;
        return getId() != null && Objects.equals(getId(), distributionOrder.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
