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
 * 满优惠活动实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:25:02
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = FullDiscount.TABLE_NAME)
@TableName(FullDiscount.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = FullDiscount.TABLE_NAME, comment = "满优惠活动实体类")
public class FullDiscount extends BasePromotions<FullDiscount, Long> {

    public static final String TABLE_NAME = "tt_full_discount";
    /** 优惠门槛金额 */
    @Column(name = "full_money", columnDefinition = "decimal(10,2) not null default 0 comment '优惠门槛金额'")
    private BigDecimal fullMoney;
    /** 活动是否减现金 */
    @Column(name = "is_full_minus", columnDefinition = "boolean not null default false comment '活动是否减现金'")
    private Boolean isFullMinus;
    /** 减现金 */
    @Column(name = "full_minus", columnDefinition = "decimal(10,2) not null default 0 comment '减现金'")
    private BigDecimal fullMinus;
    /** 是否打折 */
    @Column(name = "is_full_rate", columnDefinition = "boolean not null default false  comment '是否打折'")
    private Boolean isFullRate;
    /** 打折 */
    @Column(name = "full_rate", columnDefinition = "decimal(10,2) not null default 0 comment '打折'")
    private BigDecimal fullRate;
    /** 是否赠送积分 */
    @Column(name = "is_point", columnDefinition = "boolean not null default false comment '是否赠送积分'")
    private Boolean isPoint;
    /** 赠送多少积分 */
    @Column(name = "point", columnDefinition = "int not null default 0 comment '赠送多少积分'")
    private Integer point;
    /** 是否包邮 */
    @Column(name = "is_free_freight", columnDefinition = "boolean not null default false comment '是否包邮'")
    private Boolean isFreeFreight;
    /** 是否有赠品 */
    @Column(name = "is_gift", columnDefinition = "boolean not null default false comment '是否有赠品'")
    private Boolean isGift;
    /** 赠品id */
    @Column(name = "gift_id", columnDefinition = "bigint not null comment '赠品id'")
    private Long giftId;
    /** 是否赠优惠券 */
    @Column(name = "is_coupon", columnDefinition = "boolean not null comment '是否赠优惠券'")
    private Boolean isCoupon;
    /** 优惠券id */
    @Column(name = "coupon_id", columnDefinition = "bigint not null comment '优惠券id'")
    private Long couponId;
    /** 活动标题 */
    @Column(name = "title", columnDefinition = "varchar(255) not null comment '活动标题'")
    private String title;
    /** 活动说明 */
    @Column(name = "description", columnDefinition = "varchar(255) not null comment '活动说明'")
    private String description;

    public Boolean getIsFullMinus() {
        if (isFullMinus == null) {
            return false;
        }
        return isFullMinus;
    }

    public Boolean getIsFullRate() {
        if (isFullRate == null) {
            return false;
        }
        return isFullRate;
    }

    public Boolean getIsPoint() {
        if (isPoint == null) {
            return false;
        }
        return isPoint;
    }

    public Boolean getIsFreeFreight() {
        if (isFreeFreight == null) {
            return false;
        }
        return isFreeFreight;
    }

    public Boolean getIsGift() {
        if (isGift == null) {
            return false;
        }
        return isGift;
    }

    public Boolean getIsCoupon() {
        if (isCoupon == null) {
            return false;
        }
        return isCoupon;
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
