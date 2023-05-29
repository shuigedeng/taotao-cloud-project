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

package com.taotao.cloud.store.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.store.api.enums.BillStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

/** 结算清单表 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = Bill.TABLE_NAME)
@TableName(Bill.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
// @org.hibernate.annotations.Table(appliesTo = Bill.TABLE_NAME, comment = "结算清单表")
public class Bill extends BaseSuperEntity<Bill, String> {

    public static final String TABLE_NAME = "tt_bill";

    @Column(name = "sn", columnDefinition = "varchar(64) not null comment '账单号'")
    private String sn;

    @Column(name = "start_time", columnDefinition = "datetime null comment '结算开始时间'")
    private LocalDateTime startTime;

    @Column(name = "end_time", columnDefinition = "datetime null comment '结算结束时间'")
    private LocalDateTime endTime;

    /**
     * @see BillStatusEnum
     */
    @Column(
            name = "bill_status",
            columnDefinition = "varchar(32) not null comment '状态：OUT(已出账),CHECK(已对账),EXAMINE(已审核),PAY(已付款)'")
    private String billStatus;

    @Column(name = "store_id", columnDefinition = "varchar(32) not null comment '店铺id'")
    private Long storeId;

    @Column(name = "store_name", columnDefinition = "varchar(32) not null comment '店铺名称'")
    private String storeName;

    @Column(name = "pay_time", columnDefinition = "TIMESTAMP comment '平台付款时间'")
    private LocalDateTime payTime;

    @Column(name = "bank_account_name", columnDefinition = "varchar(32) not null comment '银行开户名'")
    private String bankAccountName;

    @Column(name = "bank_account_number", columnDefinition = "varchar(32) not null comment '公司银行账号'")
    private String bankAccountNumber;

    @Column(name = "bank_name", columnDefinition = "varchar(32) not null comment '开户银行支行名称'")
    private String bankName;

    @Column(name = "bank_code", columnDefinition = "varchar(32) not null comment '支行联行号'")
    private String bankCode;

    /**
     * 算钱规则 billPrice=orderPrice-refundPrice -commissionPrice+refundCommissionPrice
     * -distributionCommission+distributionRefundCommission
     * +siteCouponCommission-siteCouponRefundCommission +kanjiaSettlementPrice+pointSettlementPrice
     */
    @Column(name = "order_price", columnDefinition = "decimal(10,2) not null default 0 comment '结算周期内订单付款总金额'")
    private BigDecimal orderPrice;

    @Column(name = "refund_price", columnDefinition = "decimal(10,2) not null default 0 comment '退单金额'")
    private BigDecimal refundPrice;

    @Column(name = "commission_price", columnDefinition = "decimal(10,2) not null default 0 comment '平台收取佣金'")
    private BigDecimal commissionPrice;

    @Column(
            name = "refund_commission_price",
            columnDefinition = "decimal(10,2) not null default 0 comment '退单产生退还佣金金额'")
    private BigDecimal refundCommissionPrice;

    @Column(name = "distribution_commission", columnDefinition = "decimal(10,2) not null default 0 comment '分销返现支出'")
    private BigDecimal distributionCommission;

    @Column(
            name = "distribution_refund_commission",
            columnDefinition = "decimal(10,2) not null default 0 comment '分销订单退还，返现佣金返还'")
    private BigDecimal distributionRefundCommission;

    @Column(name = "site_coupon_commission", columnDefinition = "decimal(10,2) not null default 0 comment '平台优惠券补贴'")
    private BigDecimal siteCouponCommission;

    @Column(
            name = "site_coupon_refund_commission",
            columnDefinition = "decimal(10,2) not null default 0 comment '退货平台优惠券补贴返还'")
    private BigDecimal siteCouponRefundCommission;

    @Column(name = "point_settlement_price", columnDefinition = "decimal(10,2) not null default 0 comment '积分商品结算价格'")
    private BigDecimal pointSettlementPrice;

    @Column(name = "kanjia_settlement_price", columnDefinition = "decimal(10,2) not null default 0 comment '砍价商品结算价格'")
    private BigDecimal kanjiaSettlementPrice;

    @Column(name = "bill_price", columnDefinition = "decimal(10,2) not null default 0 comment '最终结算金额'")
    private BigDecimal billPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Bill dict = (Bill) o;
        return getId() != null && Objects.equals(getId(), dict.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
