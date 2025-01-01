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
 * 退款流水表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:47
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderRefundPaySeqPO.TABLE_NAME)
@Table(name = OrderRefundPaySeqPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderRefundPaySeqPO.TABLE_NAME, comment = "退款流水表")
public class OrderRefundPaySeqPO extends BaseSuperEntity<OrderRefundPaySeqPO, Long> {

    public static final String TABLE_NAME = "order_refund_pay_seq";

    /** 售后申请ID */
    @Column(name = "refund_code", columnDefinition = "varchar(32) not null comment '售后申请ID'")
    private String refundCode;

    /** 管家审核日期 */
    @Column(name = "steward_audit_date", columnDefinition = "TIMESTAMP comment '管家审核日期'")
    private LocalDateTime stewardAuditDate;

    /** 管家id */
    @Column(name = "steward_id", columnDefinition = "bigint not null comment '管家id'")
    private Long stewardId;

    /** 退款金额 */
    @Column(name = "amount", columnDefinition = "decimal(10,2) not null default 0 comment '退款金额'")
    private BigDecimal amount;

    /** 微信退款ID */
    @Column(name = "wx_refund_id", columnDefinition = "varchar(32) not null comment '微信退款ID'")
    private String wxRefundId;

    /** 微信退款渠道 需要通过微信 “查询退款”接口设置 */
    @Column(name = "wx_refund_chanel", columnDefinition = "varchar(32) not null comment '微信退款渠道 需要通过微信 “查询退款”接口设置'")
    private String wxRefundChanel;

    /** 微信退款状态 需要通过微信 “查询退款”接口设置 */
    @Column(name = "wx_refund_status", columnDefinition = "int not null default 0 comment ' 微信退款状态 需要通过微信 “查询退款”接口设置'")
    private Integer wxRefundStatus;

    /** 微信退款收款账户 需要通过微信 “查询退款”接口设置 */
    @Column(name = "wx_refund_target", columnDefinition = "varchar(32) not null comment '微信退款收款账户 需要通过微信 “查询退款”接口设置'")
    private String wxRefundTarget;

    /** 退款时间 */
    @Column(name = "refund_date", columnDefinition = "datetime comment '退款时间'")
    private LocalDateTime refundDate;

    /** 创建日期 */
    @Column(name = "create_date", columnDefinition = "datetime comment '创建日期'")
    private LocalDateTime createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderRefundPaySeqPO that = (OrderRefundPaySeqPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
