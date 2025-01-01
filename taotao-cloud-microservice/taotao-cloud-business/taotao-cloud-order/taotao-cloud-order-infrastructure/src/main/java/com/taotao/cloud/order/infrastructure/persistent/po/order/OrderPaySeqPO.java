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
import com.taotao.boot.webagg.entity.BaseSuperEntity;
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
 * 订单支付流水表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:50
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderPaySeqPO.TABLE_NAME)
@Table(name = OrderPaySeqPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderPaySeqPO.TABLE_NAME, comment = "订单支付流水表")
public class OrderPaySeqPO extends BaseSuperEntity<OrderPaySeqPO, Long> {

    public static final String TABLE_NAME = "order_pay_seq";

    /** 支付流水编码--需要与微信的预支付ID进行关联 */
    @Column(name = "pay_code", columnDefinition = "varchar(32) not null comment '支付流水编码'")
    private String payCode;

    /** 买家ID */
    @Column(name = "customer_id", columnDefinition = "bigint not null comment '买家ID'")
    private Long customerId;

    /** 付款方银行编码 */
    @Column(name = "payer_bank_code", columnDefinition = "varchar(32) not null comment '付款方银行编码'")
    private String payerBankCode;

    /** 交易金额 */
    @Column(name = "actual_amount", columnDefinition = "decimal(10,2) not null default 0 comment '交易金额'")
    private BigDecimal actualAmount;

    /** 微信预支付ID */
    @Column(name = "prepay_id", columnDefinition = "varchar(32) not null comment '微信预支付ID'")
    private String prepayId;

    /** 微信交易ID */
    @Column(name = "transaction_id", columnDefinition = "varchar(32) not null comment '微信交易ID'")
    private String transactionId;

    /** 微信商户ID */
    @Column(name = "mch_id", columnDefinition = "varchar(32) not null comment '微信商户ID'")
    private String mchId;

    /** 微信APPID */
    @Column(name = "app_id", columnDefinition = "varchar(32) not null comment '微信APPID'")
    private String appId;

    /** 状态 0-等待支付 1-超时关闭 2-支付失败 3-支付成功 */
    @Column(name = "status", columnDefinition = "int not null default 0 comment '状态 0-等待支付 1-超时关闭 2-支付失败 3-支付成功'")
    private Integer status;

    /** 备注 */
    @Column(name = "remark", columnDefinition = "varchar(3200) comment '备注'")
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderPaySeqPO that = (OrderPaySeqPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
