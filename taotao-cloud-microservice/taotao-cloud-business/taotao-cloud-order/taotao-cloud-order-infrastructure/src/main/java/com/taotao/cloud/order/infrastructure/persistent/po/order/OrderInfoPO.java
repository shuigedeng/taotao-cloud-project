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
import com.taotao.cloud.order.api.constant.OrderConstant;
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
 * 订单信息表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:33
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderInfoPO.TABLE_NAME)
@Table(name = OrderInfoPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderInfoPO.TABLE_NAME, comment = "订单信息表")
public class OrderInfoPO extends BaseSuperEntity<OrderInfoPO, Long> {

    public static final String TABLE_NAME = "order_info";

    /** 买家ID */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '买家ID'")
    private Long memberId;

    /** 优惠券id */
    @Column(name = "coupon_id", columnDefinition = "bigint comment '优惠券id'")
    private Long couponId;

    /** 秒杀活动id */
    @Column(name = "seckill_id", columnDefinition = "bigint comment '秒杀活动id'")
    private Long seckillId;

    /** 订单编码 */
    @Column(name = "code", unique = true, columnDefinition = "varchar(32) not null comment '订单编码'")
    private String code;

    /** 订单金额 */
    @Column(name = "amount", columnDefinition = "decimal(10,2) not null default 0 comment '订单金额'")
    private BigDecimal amount = BigDecimal.ZERO;

    /** 优惠金额 */
    @Column(name = "discount_amount", columnDefinition = "decimal(10,2) default 0 comment '优惠金额'")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /** 实际支付金额 */
    @Column(name = "actual_amount", columnDefinition = "decimal(10,2) default 0 comment '实际支付金额'")
    private BigDecimal actualAmount = BigDecimal.ZERO;

    /** 支付时间--支付成功后的时间 */
    @Column(name = "pay_success_time", columnDefinition = "TIMESTAMP comment '支付时间--支付成功后的时间'")
    private LocalDateTime paySuccessTime;

    /**
     * 订单主状态
     *
     * @see OrderConstant
     */
    @Column(name = "main_status", columnDefinition = "int not null comment '订单主状态'")
    private Integer mainStatus;

    /**
     * 订单子状态
     *
     * @see OrderConstant
     */
    @Column(name = "child_status", columnDefinition = "int not null comment '订单子状态'")
    private Integer childStatus;

    /** 售后主状态 */
    @Column(name = "refund_main_status", columnDefinition = "int not null default 0 comment '售后主状态'")
    private Integer refundMainStatus = 0;

    /** 售后子状态 */
    @Column(name = "refund_child_status", columnDefinition = "int not null default 0 comment '售后子状态'")
    private Integer refundChildStatus = 0;

    /**
     * 是否可评价 <br>
     * 不可评价 --0 <br>
     * 可评价 --1 <br>
     * 可追评 --2
     */
    @Column(name = "evaluate_status", columnDefinition = "int not null default 0 comment '评价状态 0-不可评价 1-可评价 2-可追评'")
    private Integer evaluateStatus = 0;

    /** 申请售后code */
    @Column(name = "refund_code", unique = true, columnDefinition = "varchar(32) comment '申请售后code'")
    private String refundCode;

    /** 申请售后是否撤销 1--已撤销 0--未撤销 */
    @Column(name = "has_cancel", columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '申请售后是否撤销 1-已撤销 0-未撤销'")
    private Boolean hasCancel = false;

    /** 发货时间 */
    @Column(name = "ship_time", columnDefinition = "TIMESTAMP comment '发货时间'")
    private LocalDateTime shipTime;

    /** 收货时间 */
    @Column(name = "receipt_time", columnDefinition = "TIMESTAMP comment '收货时间'")
    private LocalDateTime receiptTime;

    /** 交易结束时间--(1.每天00：15定时任务订单自动取消时间 2.用户收货后收货时间) */
    @Column(name = "trade_end_time", columnDefinition = "TIMESTAMP comment '交易结束时间'")
    private LocalDateTime tradeEndTime;

    /** 收货人姓名 */
    @Column(name = "receiver_name", columnDefinition = "varchar(32) not null comment '收货人姓名'")
    private String receiverName;

    /** 收货人电话 */
    @Column(name = "receiver_phone", columnDefinition = "varchar(32) not null comment '收货人电话'")
    private String receiverPhone;

    /** 收货地址:json的形式存储 {"province":"省","city":"市","zone":"区","detail":"详细地址"} */
    @Column(name = "receiver_address_json", columnDefinition = "json not null comment '收货地址:json的形式存储'")
    private String receiverAddressJson;

    /** 冗余收货地址字符串 */
    @Column(name = "receiver_address", columnDefinition = "varchar(2550) comment '冗余收货地址字符串'")
    private String receiverAddress;

    /** 买家留言 */
    @Column(name = "member_msg", columnDefinition = "varchar(255) comment '买家留言'")
    private String memberMsg;

    /** 取消订单说明 */
    @Column(name = "cancel_msg", columnDefinition = "varchar(255) comment '取消订单说明'")
    private String cancelMsg;

    /** 物流公司code */
    @Column(name = "express_code", columnDefinition = "varchar(32) comment '物流公司code'")
    private String expressCode;

    /** 物流公司名称 */
    @Column(name = "express_name", columnDefinition = "varchar(32) comment '物流公司名称'")
    private String expressName;

    /** 物流单号 */
    @Column(name = "express_number", columnDefinition = "varchar(32) comment '物流单号'")
    private String expressNumber;

    /** 买家IP */
    @Column(name = "member_ip", columnDefinition = "varchar(32) comment '买家IP'")
    private String memberIp;

    /** 是否结算 0-未结算，1-已结算 */
    @Column(name = "has_settlement", columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否结算 0-未结算，1-已结算'")
    private Boolean hasSettlement = false;

    /** 订单类型 */
    @Column(name = "type", columnDefinition = "int not null default 0 comment '订单类型 0-普通订单 1-秒杀订单'")
    private Integer type = 0;

    /** 条形码 */
    @Column(name = "bar_code", columnDefinition = "varchar(32) comment '条形码'")
    private String barCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderInfoPO orderInfoPO = (OrderInfoPO) o;
        return getId() != null && Objects.equals(getId(), orderInfoPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
