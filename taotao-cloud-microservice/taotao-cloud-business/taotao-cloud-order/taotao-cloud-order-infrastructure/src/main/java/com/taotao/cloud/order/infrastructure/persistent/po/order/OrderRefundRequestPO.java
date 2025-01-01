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
 * 售后申请表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:56
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderRefundRequestPO.TABLE_NAME)
@Table(name = OrderRefundRequestPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderRefundRequestPO.TABLE_NAME, comment = "售后申请表")
public class OrderRefundRequestPO extends BaseSuperEntity<OrderRefundRequestPO, Long> {

    public static final String TABLE_NAME = "order_refund_request";

    /** 退款编号 */
    @Column(name = "refund_code", columnDefinition = "varchar(32) not null comment '退款编号'")
    private String refundCode;

    /** 售后类型 1, 仅退款;0, 退货退款 */
    @Column(name = "refund_type", columnDefinition = "int not null default 0 comment ' 售后类型 1, 仅退款;0, 退货退款'")
    private Integer refundType;

    /** 用户物流收货类型：未收到货--3;已收到货--4 */
    @Column(name = "receive_status", columnDefinition = "int not null default 0 comment ' 用户物流收货类型：未收到货--3;已收到货--4'")
    private Integer receiveStatus;

    /** 主状态 10, 关闭;20, 处理中;30, 退款成功 */
    @Column(name = "main_status", columnDefinition = "int not null default 0 comment ' 主状态 10, 关闭;20, 处理中;30, 退款成功'")
    private Integer mainStatus;

    /** 子状态 */
    @Column(name = "child_status", columnDefinition = "int not null default 0 comment ' 子状态'")
    private Integer childStatus;

    /** 用户退货状态 0, 无需退货,10, 未退货，20, 已退货 */
    @Column(
            name = "user_return_goods_status",
            columnDefinition = "int not null default 0 comment '  用户退货状态 0, 无需退货,10, 未退货，20, 已退货'")
    private Integer userReturnGoodsStatus;

    /** 申请原因 */
    @Column(name = "refund_cause", columnDefinition = "varchar(256) null comment '申请原因'")
    private String refundCause;

    /** 退款说明 */
    @Column(name = "refund_remark", columnDefinition = "varchar(2560) not null comment '退款说明'")
    private String refundRemark;

    /** 订单总金额 */
    @Column(name = "total_amount", columnDefinition = "decimal(10,2) not null default 0 comment '订单总金额'")
    private BigDecimal totalAmount;

    /** 申请退款金额 */
    @Column(name = "req_refund_amount", columnDefinition = "decimal(10,2) not null default 0 comment '申请退款金额'")
    private BigDecimal reqRefundAmount;

    /** 实际退款金额--定时任务设置 */
    @Column(name = "act_refund_amount", columnDefinition = "decimal(10,2) not null default 0 comment '实际退款金额--定时任务设置'")
    private BigDecimal actRefundAmount;

    /** 买家会员ID */
    @Column(name = "customer_id", columnDefinition = "varchar(256) not null comment '申请原因'")
    private String customerId;

    /** 买家姓名 */
    @Column(name = "customer_name", columnDefinition = "varchar(256) not null comment '买家姓名'")
    private String customerName;

    /** 买家电话 */
    @Column(name = "customer_phone", columnDefinition = "varchar(256) not null comment '买家电话'")
    private String customerPhone;

    /** 收货人名称 */
    @Column(name = "receiver_name", columnDefinition = "varchar(256) not null comment '收货人名称'")
    private String receiverName;

    /** 收货人电话 */
    @Column(name = "receiver_phone", columnDefinition = "varchar(256) not null comment '收货人电话'")
    private String receiverPhone;

    /** 收货人地区 */
    @Column(name = "receiver_address", columnDefinition = "varchar(256) not null comment '收货人地区'")
    private String receiverAddress;

    /** 申请时间 */
    @Column(name = "create_Date", columnDefinition = "TIMESTAMP comment '创建时间'")
    private LocalDateTime subDate;

    /** 结束关闭时间 */
    @Column(name = "end_date", columnDefinition = "TIMESTAMP comment '结束关闭时间'")
    private LocalDateTime endDate;

    /** 管家处理时间 */
    @Column(name = "steward_date", columnDefinition = "TIMESTAMP comment '管家处理时间'")
    private LocalDateTime stewardDate;

    /** 用户退货时间 */
    @Column(name = "return_goods_date", columnDefinition = "TIMESTAMP comment '用户退货时间'")
    private LocalDateTime returnGoodsDate;

    /** 退款确认时间 */
    @Column(name = "mch_handle_date", columnDefinition = "TIMESTAMP comment '退款确认时间'")
    private LocalDateTime mchHandleDate;

    /** 退款成功时间 */
    @Column(name = "return_money_date", columnDefinition = "TIMESTAMP comment '退款成功时间'")
    private LocalDateTime returnMoneyDate;

    /** 商品SPU ID */
    @Column(name = "product_spu_id", columnDefinition = "varchar(256) not null comment '商品SPU ID'")
    private String productSpuId;

    /** 商品SPU名称 */
    @Column(name = "product_spu_name", columnDefinition = "varchar(256) not null comment '商品SPU名称'")
    private String productSpuName;

    /** 商品SKU ID */
    @Column(name = "product_sku_id", columnDefinition = "varchar(256) not null comment '商品SKU ID'")
    private String productSkuId;

    /** 商品SKU 规格名称 */
    @Column(name = "product_sku_name", columnDefinition = "varchar(256) not null comment ' 商品SKU 规格名称'")
    private String productSkuName;

    /** 商品单价 */
    @Column(name = "product_price", columnDefinition = "decimal(10,2) not null default 0 comment '商品单价'")
    private BigDecimal productPrice = BigDecimal.ZERO;

    /** 购买数量 */
    @Column(name = "buy_num", columnDefinition = "int not null default 0 comment '购买数量'")
    private Integer buyNum;

    /** 管家编码 */
    @Column(name = "steward_id", columnDefinition = "varchar(256) not null comment '管家编码'")
    private String stewardId;

    /** 管家名称 */
    @Column(name = "steward_name", columnDefinition = "varchar(256) not null comment '管家名称'")
    private String stewardName;

    /** 管家昵称 */
    @Column(name = "steward_nick", columnDefinition = "varchar(256) not null comment '管家昵称'")
    private String stewardNick;

    /** 关闭理由 */
    @Column(name = "close_cause", columnDefinition = "varchar(256) not null comment '关闭理由'")
    private String closeCause;

    /** 关闭说明 */
    @Column(name = "close_remark", columnDefinition = "varchar(256) not null comment '关闭说明'")
    private String closeRemark;

    /** 物流公司编码 */
    @Column(name = "express_code", columnDefinition = "varchar(256) not null comment '物流公司编码'")
    private String expressCode;

    /** 物流快递单号 */
    @Column(name = "post_id", columnDefinition = "varchar(256) not null comment '物流快递单号'")
    private String postId;

    /** 物流说明 */
    @Column(name = "express_remark", columnDefinition = "varchar(256) not null comment '物流说明'")
    private String expressRemark;

    /** 此售后是否存在撤销 1 -存在撤销 0 -不存在撤销 */
    @Column(name = "has_cancel", columnDefinition = "int not null default 0 comment '此售后是否存在撤销 1 -存在撤销 0 -不存在撤销'")
    private Integer hasCancel;

    @Column(name = "order_code", columnDefinition = "varchar(256) not null comment 'orderCode'")
    private String orderCode;

    /** 订单支付流水号 */
    @Column(name = "pay_seq_code", columnDefinition = "varchar(256) not null comment '订单支付流水号'")
    private String paySeqCode;

    /** 微信订单号. */
    @Column(name = "transaction_id", columnDefinition = "varchar(256) not null comment '微信订单号'")
    private String transactionId;

    /** 商城id */
    @Column(name = "mall_id", columnDefinition = "int not null default 0 comment '商城id'")
    private Integer mallId;

    /** 供应商id */
    @Column(name = "supplier_id", columnDefinition = "int not null default 0 comment '供应商id'")
    private Integer supplierId;

    /** 场景方id */
    @Column(name = "scene_id", columnDefinition = "int not null default 0 comment '场景方id'")
    private Integer sceneId;

    @Column(name = "collect_scene_confirm_date", columnDefinition = "TIMESTAMP comment 'collectSceneConfirmDate'")
    private LocalDateTime collectSceneConfirmDate;

    @Column(name = "org_id", columnDefinition = "int not null default 0 comment 'orgId'")
    private Integer orgId;

    @Column(name = "order_type", columnDefinition = "int not null default 0 comment 'orderType'")
    private Integer orderType;

    @Column(name = "item_code", columnDefinition = "varchar(256) not null comment 'itemCode'")
    private String itemCode;

    @Column(name = "main_code", columnDefinition = "varchar(256) not null comment 'mainCode'")
    private String mainCode;

    @Column(name = "has_apply", columnDefinition = "int not null default 0 comment 'hasApply'")
    private Integer hasApply;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderRefundRequestPO that = (OrderRefundRequestPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
