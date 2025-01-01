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

package com.taotao.cloud.order.infrastructure.persistent.po.aftersale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * 售后表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:00:45
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AfterSalePO.TABLE_NAME)
@TableName(AfterSalePO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = AfterSale.TABLE_NAME, comment = "售后表")
public class AfterSalePO extends BaseSuperEntity<AfterSalePO, Long> {

    public static final String TABLE_NAME = "tt_after_sale";

    /** 售后服务单号 */
    @Column(name = "sn", columnDefinition = "varchar(64) null comment '售后服务单号'")
    private String sn;

    /** 订单编号 */
    @Column(name = "order_sn", columnDefinition = "varchar(64) null comment '订单编号'")
    private String orderSn;

    /** 订单货物编号 */
    @Column(name = "order_item_sn", columnDefinition = "varchar(64) null comment '订单货物编号'")
    private String orderItemSn;

    /** 交易编号 */
    @Column(name = "trade_sn", columnDefinition = "varchar(64) not null comment '交易编号'")
    private String tradeSn;

    /** 会员ID */
    @Column(name = "member_id", columnDefinition = "bigint null comment '会员ID'")
    private Long memberId;

    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(64) null comment '会员名称'")
    private String memberName;

    /** 商家ID */
    @Column(name = "store_id", columnDefinition = "bigint null comment '商家ID'")
    private Long storeId;

    /** 商家名称 */
    @Column(name = "store_name", columnDefinition = "varchar(64) null comment '商家名称'")
    private String storeName;

    // ****************商品信息************

    /** 商品ID */
    @Column(name = "goods_id", columnDefinition = "bigint null comment '商品ID'")
    private Long goodsId;

    /** 货品ID */
    @Column(name = "sku_id", columnDefinition = "bigint null comment '货品ID'")
    private Long skuId;

    /** 申请数量 */
    @Column(name = "num", columnDefinition = "int null comment '申请数量'")
    private Integer num;

    /** 商品图片 */
    @Column(name = "goods_image", columnDefinition = "text null comment '商品图片'")
    private String goodsImage;

    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(255) null comment '商品名称'")
    private String goodsName;

    /** 规格json */
    @Column(name = "specs", columnDefinition = "json null comment '规格json'")
    private String specs;

    /** 实际金额 */
    @Column(name = "flow_price", columnDefinition = "decimal(10,2) null comment '实际金额'")
    private BigDecimal flowPrice;

    // ***************交涉信息************

    /** 申请原因 */
    @Schema(description = "申请原因")
    @Column(name = "reason", columnDefinition = "varchar(255) null comment '申请原因'")
    private String reason;

    /** 问题描述 */
    @Column(name = "problem_desc", columnDefinition = "text null comment '问题描述'")
    private String problemDesc;

    /** 评价图片 */
    @Column(name = "after_sale_image", columnDefinition = "text null comment '评价图片'")
    private String afterSaleImage;

    /**
     * 售后类型
     *
     * @see AfterSaleTypeEnum
     */
    @Schema(description = "售后类型", allowableValues = "RETURN_GOODS,RETURN_MONEY")
    @Column(name = "service_type", columnDefinition = "varchar(64) null comment '售后类型'")
    private String serviceType;

    /**
     * 售后单状态
     *
     * @see AfterSaleStatusEnum
     */
    @Schema(
            description = "售后单状态",
            allowableValues = "APPLY,PASS,REFUSE,BUYER_RETURN,SELLER_RE_DELIVERY,BUYER_CONFIRM,SELLER_CONFIRM,COMPLETE")
    @Column(name = "service_status", columnDefinition = "varchar(64) null comment '售后单状态'")
    private String serviceStatus;

    // *************退款信息***************

    /**
     * 退款方式
     *
     * @see AfterSaleRefundWayEnum
     */
    @Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
    @Column(name = "refund_way", columnDefinition = "varchar(64) null comment '退款方式'")
    private String refundWay;

    /** 账号类型 */
    @Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER")
    @Column(name = "account_type", columnDefinition = "varchar(64) null comment '账号类型'")
    private String accountType;

    /** 银行账户 */
    @Column(name = "bank_account_number", columnDefinition = "varchar(64) null comment '银行账户'")
    private String bankAccountNumber;

    /** 银行开户名 */
    @Column(name = "bank_account_name", columnDefinition = "varchar(64) null comment '银行开户名'")
    private String bankAccountName;

    /** 银行开户行 */
    @Column(name = "bank_deposit_name", columnDefinition = "varchar(64) null comment '银行开户行'")
    private String bankDepositName;

    /** 商家备注 */
    @Column(name = "audit_remark", columnDefinition = "varchar(255) null comment '商家备注'")
    private String auditRemark;

    /** 订单支付方式返回的交易号 */
    @Column(name = "pay_order_no", columnDefinition = "varchar(64) null comment '订单支付方式返回的交易号'")
    private String payOrderNo;

    /** 申请退款金额 */
    @Column(name = "apply_refund_price", columnDefinition = "decimal(10,2) null comment '申请退款金额'")
    private BigDecimal applyRefundPrice;

    /** 实际退款金额 */
    @Column(name = "actual_refund_price", columnDefinition = "decimal(10,2) null comment '实际退款金额'")
    private BigDecimal actualRefundPrice;

    /** 退还积分 */
    @Column(name = "refund_point", columnDefinition = "int null comment '退还积分'")
    private Integer refundPoint;

    /** 退款时间 */
    @Column(name = "refund_time", columnDefinition = "DATETIME null comment '退款时间'")
    private LocalDateTime refundTime;

    // *****************买家物流信息****************

    /** 发货单号 */
    @Column(name = "logistics_no", columnDefinition = "varchar(64) null comment '发货单号'")
    private String logisticsNo;

    /** 物流公司CODE */
    @Column(name = "logistics_code", columnDefinition = "varchar(64) null comment '物流公司CODE'")
    private String logisticsCode;

    /** 物流公司名称 */
    @Column(name = "logistics_name", columnDefinition = "varchar(64) null comment '物流公司名称'")
    private String logisticsName;

    /** 买家发货时间 */
    @Column(name = "deliver_time", columnDefinition = "DATETIME null comment '买家发货时间'")
    private LocalDateTime deliverTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AfterSalePO afterSale = (AfterSalePO) o;
        return getId() != null && Objects.equals(getId(), afterSale.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
