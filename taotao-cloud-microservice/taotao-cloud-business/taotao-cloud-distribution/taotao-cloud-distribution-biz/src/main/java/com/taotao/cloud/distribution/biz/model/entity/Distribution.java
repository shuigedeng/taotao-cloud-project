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
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.distribution.api.enums.DistributionStatusEnum;
import com.taotao.cloud.distribution.api.model.dto.DistributionApplyDTO;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
 * 分销员表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 14:59:27
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = Distribution.TABLE_NAME)
@TableName(Distribution.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Distribution.TABLE_NAME, comment = "分销员表")
public class Distribution extends BaseSuperEntity<Distribution, Long> {

    public static final String TABLE_NAME = "tt_distribution";

    public Distribution(Long memberId, String memberName, DistributionApplyDTO distributionApplyDTO) {
        this.memberId = memberId;
        this.memberName = memberName;
        distributionOrderCount = 0;
        this.distributionStatus = DistributionStatusEnum.APPLY.name();
        BeanUtils.copyProperties(distributionApplyDTO, this);
    }

    /** 会员id */
    @Column(name = "member_id", columnDefinition = "bigint not null  comment '会员id'")
    private Long memberId;
    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(255) not null  comment '会员名称'")
    private String memberName;
    /** 会员姓名 */
    @Column(name = "name", columnDefinition = "varchar(255) not null  comment '会员姓名'")
    private String name;
    /** 身份证号 */
    @Column(name = "id_number", columnDefinition = "varchar(255) not null  comment '身份证号'")
    private String idNumber;
    /** 分销总额 */
    @Column(name = "rebate_total", columnDefinition = "decimal(10,2) not null  comment '分销总额'")
    @Builder.Default
    private BigDecimal rebateTotal = BigDecimal.ZERO;
    /** 可提现金额 */
    @Column(name = "can_rebate", columnDefinition = "decimal(10,2) not null  comment '可提现金额'")
    @Builder.Default
    private BigDecimal canRebate = BigDecimal.ZERO;
    /** 冻结金额 */
    @Column(name = "commission_frozen", columnDefinition = "decimal(10,2) not null  comment '冻结金额'")
    @Builder.Default
    private BigDecimal commissionFrozen = BigDecimal.ZERO;
    /** 分销订单数 */
    @Column(name = "distribution_order_count", columnDefinition = "int not null  comment '分销订单数'")
    private Integer distributionOrderCount;

    /**
     * 分销员状态
     *
     * @see DistributionStatusEnum
     */
    @Column(name = "distribution_status", columnDefinition = "varchar(255) not null  comment '分销员状态'")
    private String distributionStatus;
    /** 结算银行开户行名称 */
    @Column(name = "settlement_bank_account_name", columnDefinition = "varchar(255) not null  comment '结算银行开户行名称'")
    private String settlementBankAccountName;
    /** 结算银行开户账号 */
    @Column(name = "settlement_bank_account_num", columnDefinition = "varchar(255) not null  comment '结算银行开户账号'")
    private String settlementBankAccountNum;
    /** 结算银行开户支行名称 */
    @Column(name = "settlement_bank_branch_name", columnDefinition = "varchar(255) not null  comment '结算银行开户支行名称'")
    private String settlementBankBranchName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Distribution distribution = (Distribution) o;
        return getId() != null && Objects.equals(getId(), distribution.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
