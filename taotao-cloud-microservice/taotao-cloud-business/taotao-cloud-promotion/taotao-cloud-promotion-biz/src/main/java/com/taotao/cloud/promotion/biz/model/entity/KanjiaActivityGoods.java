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

package com.taotao.cloud.promotion.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

/**
 * 砍价活动商品实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:53
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = KanjiaActivityGoods.TABLE_NAME)
@TableName(KanjiaActivityGoods.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = KanjiaActivityGoods.TABLE_NAME, comment = "砍价活动商品对象")
public class KanjiaActivityGoods extends BasePromotions<KanjiaActivityGoods, Long> {

    public static final String TABLE_NAME = "tt_kanjia_activity_goods";
    /** 结算价格 */
    @Column(name = "settlement_price", columnDefinition = "decimal(10,2) not null  comment '结算价格'")
    private BigDecimal settlementPrice;
    /** 商品原价 */
    @Column(name = "original_price", columnDefinition = "decimal(10,2) not null  comment '商品原价'")
    private BigDecimal originalPrice;
    /** 最低购买金额 */
    @Column(name = "purchase_price", columnDefinition = "decimal(10,2) not null  comment '最低购买金额'")
    private BigDecimal purchasePrice;
    /** 货品id */
    @Column(name = "goods_id", columnDefinition = "bigint not null  comment '货品id'")
    private Long goodsId;
    /** 货品SkuId */
    @Column(name = "sku_id", columnDefinition = "bigint not null  comment '货品SkuId'")
    private Long skuId;
    /** 货品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(255) not null  comment '货品名称'")
    private String goodsName;
    /** 缩略图 */
    @Column(name = "thumbnail", columnDefinition = "varchar(255) not null  comment '缩略图'")
    private String thumbnail;
    /** 活动库存 */
    @Column(name = "stock", columnDefinition = "int not null  comment '活动库存'")
    private Integer stock;
    /** 每人最低砍价金额 */
    @Column(name = "lowest_price", columnDefinition = "decimal(10,2) not null  comment '每人最低砍价金额'")
    private BigDecimal lowestPrice;
    /** 每人最高砍价金额 */
    @Column(name = "highest_price", columnDefinition = "decimal(10,2) not null  comment '每人最高砍价金额'")
    private BigDecimal highestPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        KanjiaActivityGoods kanjiaActivityGoods = (KanjiaActivityGoods) o;
        return getId() != null && Objects.equals(getId(), kanjiaActivityGoods.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
