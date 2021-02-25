package com.taotao.cloud.order.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单明细表
 *
 * @author dengtao
 * @date 2020/4/30 15:42
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_order_item")
@org.hibernate.annotations.Table(appliesTo = "tt_order_item", comment = "订单明细表")
public class OrderItem extends BaseEntity {

    /**
     * 订单子编码
     */
    @Column(name = "item_code", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '订单子编码'")
    private String itemCode;

    /**
     * 商品SPU ID
     */
    @Column(name = "product_spu_id", nullable = false, columnDefinition = "bigint not null comment '商品SPU ID'")
    private Long productSpuId;

    /**
     * 商品SPU_CODE
     */
    @Column(name = "product_spu_code", nullable = false, columnDefinition = "varchar(32) not null comment '商品SPU CODE'")
    private String productSpuCode;

    /**
     * 商品SPU名称
     */
    @Column(name = "product_spu_name", nullable = false, columnDefinition = "varchar(32) not null comment '商品SPU名称'")
    private String productSpuName;

    /**
     * 商品SKU ID
     */
    @Column(name = "product_sku_id", nullable = false, columnDefinition = "bigint not null comment '商品SKU ID'")
    private Long productSkuId;

    /**
     * 商品SKU 规格名称
     */
    @Column(name = "product_sku_name", nullable = false, columnDefinition = "varchar(255) not null comment '商品SKU 规格名称'")
    private String productSkuName;

    /**
     * 商品单价
     */
    @Builder.Default
    @Column(name = "product_price", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '商品单价'")
    private BigDecimal productPrice = BigDecimal.ZERO;

    /**
     * 购买数量
     */
    @Builder.Default
    @Column(name = "num", columnDefinition = "int not null default 1 comment '购买数量'")
    private Integer num = 1;

    /**
     * 合计金额
     */
    @Column(name = "sum_amount", columnDefinition = "decimal(10,2) not null default 0 comment '合计金额'")
    private BigDecimal sumAmount = BigDecimal.ZERO;

    /**
     * 商品主图
     */
    @Column(name = "product_pic_url", columnDefinition = "varchar(255) comment '商品主图'")
    private String productPicUrl;

    /**
     * 供应商id
     */
    @Column(name = "supplier_id", nullable = false, columnDefinition = "bigint not null comment '供应商id'")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @Column(name = "supplier_name", nullable = false, columnDefinition = "varchar(255) not null comment '供应商名称'")
    private String supplierName;

    /**
     * 超时退货期限
     */
    @Column(name = "refund_time", columnDefinition = "int default 0 comment '超时退货期限'")
    private Integer refundTime;

    /**
     * 退货数量
     */
    @Builder.Default
    @Column(name = "reject_count", columnDefinition = "int not null default 0 comment '退货数量'")
    private Integer rejectCount = 0;

    /**
     * 商品类型 0 普通商品 1 秒杀商品
     */
    @Column(name = "type", columnDefinition = "int not null default 0 comment '0-普通商品 1-秒杀商品'")
    private Integer type = 0;
}
