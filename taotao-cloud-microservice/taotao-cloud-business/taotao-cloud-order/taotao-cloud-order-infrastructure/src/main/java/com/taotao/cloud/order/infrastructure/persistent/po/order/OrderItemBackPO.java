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
 * 订单明细表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:38
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderItemBackPO.TABLE_NAME)
@Table(name = OrderItemBackPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderItemBackPO.TABLE_NAME, comment = "订单明细表")
public class OrderItemBackPO extends BaseSuperEntity<OrderItemBackPO, Long> {

    public static final String TABLE_NAME = "order_item";

    /** 订单子编码 */
    @Column(name = "item_code", unique = true, columnDefinition = "varchar(32) not null comment '订单子编码'")
    private String itemCode;

    /** 商品SPU ID */
    @Column(name = "product_spu_id", columnDefinition = "bigint not null comment '商品SPU ID'")
    private Long productSpuId;

    /** 商品SPU_CODE */
    @Column(name = "product_spu_code", columnDefinition = "varchar(32) not null comment '商品SPU CODE'")
    private String productSpuCode;

    /** 商品SPU名称 */
    @Column(name = "product_spu_name", columnDefinition = "varchar(32) not null comment '商品SPU名称'")
    private String productSpuName;

    /** 商品SKU ID */
    @Column(name = "product_sku_id", columnDefinition = "bigint not null comment '商品SKU ID'")
    private Long productSkuId;

    /** 商品SKU 规格名称 */
    @Column(name = "product_sku_name", columnDefinition = "varchar(255) not null comment '商品SKU 规格名称'")
    private String productSkuName;

    /** 商品单价 */
    @Column(name = "product_price", columnDefinition = "decimal(10,2) not null default 0 comment '商品单价'")
    private BigDecimal productPrice = BigDecimal.ZERO;

    /** 购买数量 */
    @Column(name = "num", columnDefinition = "int not null default 1 comment '购买数量'")
    private Integer num = 1;

    /** 合计金额 */
    @Column(name = "sum_amount", columnDefinition = "decimal(10,2) not null default 0 comment '合计金额'")
    private BigDecimal sumAmount = BigDecimal.ZERO;

    /** 商品主图 */
    @Column(name = "product_pic_url", columnDefinition = "varchar(255) comment '商品主图'")
    private String productPicUrl;

    /** 供应商id */
    @Column(name = "supplier_id", columnDefinition = "bigint not null comment '供应商id'")
    private Long supplierId;

    /** 供应商名称 */
    @Column(name = "supplier_name", columnDefinition = "varchar(255) not null comment '供应商名称'")
    private String supplierName;

    /** 超时退货期限 */
    @Column(name = "refund_time", columnDefinition = "int default 0 comment '超时退货期限'")
    private Integer refundTime;

    /** 退货数量 */
    @Column(name = "reject_count", columnDefinition = "int not null default 0 comment '退货数量'")
    private Integer rejectCount = 0;

    /** 商品类型 0 普通商品 1 秒杀商品 */
    @Column(name = "type", columnDefinition = "int not null default 0 comment '0-普通商品 1-秒杀商品'")
    private Integer type = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderItemBackPO that = (OrderItemBackPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
