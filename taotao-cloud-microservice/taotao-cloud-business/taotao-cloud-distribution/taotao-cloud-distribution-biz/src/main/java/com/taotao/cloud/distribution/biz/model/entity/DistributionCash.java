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
import com.taotao.cloud.member.api.enums.WithdrawStatusEnum;
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
 * 分销佣金
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 14:59:22
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DistributionCash.TABLE_NAME)
@TableName(DistributionCash.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = DistributionCash.TABLE_NAME, comment = "分销佣金表")
public class DistributionCash extends BaseSuperEntity<DistributionCash, Long> {

    public static final String TABLE_NAME = "tt_distribution_cash";

    /** 分销佣金sn */
    @Column(name = "sn", columnDefinition = "varchar(255) not null  comment '分销佣金sn'")
    private String sn;
    /** 分销员id */
    @Column(name = "distribution_id", columnDefinition = "bigint not null  comment '分销员id'")
    private Long distributionId;
    /** 分销员名称 */
    @Column(name = "distribution_name", columnDefinition = "varchar(255) not null  comment '分销员名称'")
    private String distributionName;
    /** 分销佣金 */
    @Column(name = "price", columnDefinition = "decimal(10,2) not null  comment '分销佣金'")
    private BigDecimal price;
    /** 支付时间 */
    @Column(name = "pay_time", columnDefinition = "datetime not null  comment '支付时间'")
    private LocalDateTime payTime;
    /** 状态 */
    @Column(name = "distribution_cash_status", columnDefinition = "varchar(255) not null  comment '状态'")
    private String distributionCashStatus;

    public DistributionCash(String sn, Long distributionId, BigDecimal price, String memberName) {
        this.sn = sn;
        this.distributionId = distributionId;
        this.price = price;
        this.distributionCashStatus = WithdrawStatusEnum.APPLY.name();
        this.distributionName = memberName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DistributionCash distributionCash = (DistributionCash) o;
        return getId() != null && Objects.equals(getId(), distributionCash.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
