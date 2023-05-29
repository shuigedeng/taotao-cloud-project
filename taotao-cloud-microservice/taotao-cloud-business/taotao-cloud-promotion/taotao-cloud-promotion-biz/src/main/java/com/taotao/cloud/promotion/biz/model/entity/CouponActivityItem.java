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
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 优惠券活动实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:25:06
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = CouponActivityItem.TABLE_NAME)
@TableName(CouponActivityItem.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = CouponActivityItem.TABLE_NAME, comment = "优惠券活动-优惠券关联实体类")
public class CouponActivityItem extends BaseSuperEntity<CouponActivityItem, Long> {

    public static final String TABLE_NAME = "tt_coupon_activity_item";
    /** 优惠券活动ID */
    @Column(name = "activity_id", columnDefinition = "bigint not null comment '优惠券活动ID'")
    private Long activityId;
    /** 优惠券ID */
    @Column(name = "coupon_id", columnDefinition = "bigint not null comment '优惠券ID'")
    private Long couponId;
    /** 优惠券数量 */
    @Column(name = "num", columnDefinition = "int not null default 0 comment '优惠券数量'")
    private Integer num;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CouponActivityItem couponActivityItem = (CouponActivityItem) o;
        return getId() != null && Objects.equals(getId(), couponActivityItem.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
