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

package com.taotao.cloud.promotion.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.CouponRangeDayEnum;
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.vo.CouponVO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

/**
 * 优惠券活动实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:25:13
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Coupon.TABLE_NAME)
@TableName(Coupon.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Coupon.TABLE_NAME, comment = "优惠券活动实体类")
public class Coupon extends BasePromotions<Coupon, Long> {

    public static final String TABLE_NAME = "tt_coupon";

    @Column(name = "coupon_name", columnDefinition = "varchar(255) not null comment '优惠券名称'")
    private String couponName;

    /**
     * POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    @Column(name = "coupon_type", columnDefinition = "varchar(255) not null comment '优惠券类型'")
    private String couponType;

    /** 面额 */
    @Column(name = "price", columnDefinition = "decimal(10,2) not null default 0 comment '面额'")
    private BigDecimal price;

    /** 折扣 */
    @Column(name = "coupon_discount", columnDefinition = "decimal(10,2) not null default 0 comment '折扣'")
    private BigDecimal couponDiscount;

    /**
     * @see CouponGetEnum 优惠券类型，分为免费领取和活动赠送
     */
    @Column(name = "get_type", columnDefinition = "varchar(255) not null comment '优惠券类型，分为免费领取和活动赠送'")
    private String getType;

    /** 店铺承担比例,平台发布时可以提供一定返点 */
    @Column(
            name = "store_commission",
            columnDefinition = "decimal(10,2) not null default 0 comment '店铺承担比例,平台发布时可以提供一定返点'")
    private BigDecimal storeCommission;

    /** 活动描述 */
    @Column(name = "description", columnDefinition = "varchar(255) not null comment '活动描述'")
    private String description;

    /** 发行数量,如果是0则是不限制 */
    @Column(name = "publish_num", columnDefinition = "int not null default 0 comment '发行数量,如果是0则是不限制'")
    private Integer publishNum;

    /** 领取限制 */
    @Column(name = "coupon_limit_num", columnDefinition = "int not null default 0 comment '领取限制'")
    private Integer couponLimitNum;

    /** 已被使用的数量 */
    @Column(name = "used_num", columnDefinition = "int not null default 0 comment '已被使用的数量'")
    private Integer usedNum;

    /** 已被领取的数量 */
    @Column(name = "received_num", columnDefinition = "int not null default 0 comment '已被领取的数量'")
    private Integer receivedNum;

    /** 消费门槛 */
    @Column(name = "consume_threshold", columnDefinition = "decimal(10,2) not null default 0 comment '消费门槛'")
    private BigDecimal consumeThreshold;

    /**
     * @see CouponRangeDayEnum 时间范围类型
     */
    @Column(name = "range_day_type", columnDefinition = "varchar(255) not null comment '时间范围类型'")
    private String rangeDayType;

    /** 有效期 */
    @Column(name = "effective_days", columnDefinition = "int not null defaultt 0  comment '有效期'")
    private Integer effectiveDays;

    public Coupon(CouponVO couponVO) {
        BeanUtils.copyProperties(couponVO, this);
    }

    @Override
    public String getPromotionStatus() {
        if (this.rangeDayType != null
                && this.rangeDayType.equals(CouponRangeDayEnum.DYNAMICTIME.name())
                && (this.effectiveDays != null && this.effectiveDays > 0 && this.effectiveDays <= 365)) {
            return PromotionsStatusEnum.START.name();
        }
        return super.getPromotionStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Coupon coupon = (Coupon) o;
        return getId() != null && Objects.equals(getId(), coupon.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
