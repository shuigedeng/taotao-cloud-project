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
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.CouponRangeDayEnum;
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 会员优惠券实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:46
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberCoupon.TABLE_NAME)
@TableName(MemberCoupon.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberCoupon.TABLE_NAME, comment = "会员优惠券")
public class MemberCoupon extends BaseSuperEntity<MemberCoupon, Long> {

    public static final String TABLE_NAME = "tt_member_coupon";
    /** 从哪个模版领取的优惠券 */
    @Column(name = "coupon_id", columnDefinition = "bigint not null  comment '从哪个模版领取的优惠券'")
    private Long couponId;
    /** 商家id，如果是平台发送，这个值为 0 */
    @Column(name = "store_id", columnDefinition = "bigint not null  comment '商家id，如果是平台发送，这个值为 0'")
    private Long storeId;
    /** 商家名称，如果是平台，这个值为 platfor */
    @Column(name = "store_name", columnDefinition = "varchar(255) not null  comment '商家名称，如果是平台，这个值为 platfor'")
    private String storeName;
    /** 面额 */
    @Column(name = "price", columnDefinition = "decimal(10,2) not null  comment '面额'")
    private BigDecimal price;
    /** 折扣 */
    @Column(name = "discount", columnDefinition = "decimal(10,2) not null  comment '折扣'")
    private BigDecimal discount;
    /** 消费门槛 */
    @Column(name = "consume_threshold", columnDefinition = "decimal(10,2) not null  comment '消费门槛'")
    private BigDecimal consumeThreshold;
    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(255) not null  comment '会员名称'")
    private String memberName;
    /** 会员id */
    @Column(name = "member_id", columnDefinition = "bigint not null  comment '会员id'")
    private Long memberId;

    /**
     * 关联范围类型
     *
     * @see PromotionsScopeTypeEnum
     */
    @Column(name = "scope_type", columnDefinition = "varchar(255) not null  comment '关联范围类型'")
    private String scopeType;

    /**
     * 活动类型 POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    @Column(name = "coupon_type", columnDefinition = "varchar(255) not null  comment '活动类型'")
    private String couponType;

    /** 范围关联的id */
    @Column(name = "scope_id", columnDefinition = "bigint not null  comment '范围关联的id'")
    private Long scopeId;
    /** 使用起始时间 */
    @Column(name = "start_time", columnDefinition = "datetime not null  comment '使用起始时间'")
    private LocalDateTime startTime;
    /** 使用截止时间 */
    @Column(name = "end_time", columnDefinition = "datetime not null  comment '使用截止时间'")
    private LocalDateTime endTime;
    /**
     * 优惠券类型，分为免费领取和活动赠送
     *
     * @see CouponGetEnum
     */
    @Column(name = "get_type", columnDefinition = "varchar(255) not null  comment '优惠券类型，分为免费领取和活动赠送'")
    private String getType;
    /** 是否是平台优惠券 */
    @Column(name = "is_platform", columnDefinition = "boolean not null  comment '是否是平台优惠券'")
    private Boolean isPlatform;
    /** 店铺承担比例 */
    @Column(name = "store_commission", columnDefinition = "decimal(10,2) not null  comment '店铺承担比例'")
    private BigDecimal storeCommission;
    /** 核销时间 */
    @Column(name = "consumption_time", columnDefinition = "datetime not null  comment '核销时间'")
    private LocalDateTime consumptionTime;

    /**
     * 会员优惠券状态
     *
     * @see MemberCouponStatusEnum
     */
    @Column(name = "member_coupon_status", columnDefinition = "varchar(255) not null  comment '会员优惠券状态'")
    private String memberCouponStatus;

    public MemberCoupon(Coupon coupon) {
        setCouponId(coupon.getId());
        setStoreId(coupon.getStoreId());
        setStoreName(coupon.getStoreName());
        setPrice(coupon.getPrice());
        setDiscount(coupon.getCouponDiscount());
        setConsumeThreshold(coupon.getConsumeThreshold());
        setScopeType(coupon.getScopeType());
        setScopeId(coupon.getScopeId());
        setCouponType(coupon.getCouponType());
        setStartTime(coupon.getStartTime() == null ? new Date() : coupon.getStartTime());

        setGetType(coupon.getGetType());
        setStoreCommission(coupon.getStoreCommission());
        if (coupon.getRangeDayType().equals(CouponRangeDayEnum.FIXEDTIME.name())) {
            setEndTime(coupon.getEndTime());
        } else {
            setEndTime(DateUtil.endOfDay(
                    DateUtil.offset(new DateTime(), DateField.DAY_OF_YEAR, (coupon.getEffectiveDays() - 1))));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        MemberCoupon memberCoupon = (MemberCoupon) o;
        return getId() != null && Objects.equals(getId(), memberCoupon.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
