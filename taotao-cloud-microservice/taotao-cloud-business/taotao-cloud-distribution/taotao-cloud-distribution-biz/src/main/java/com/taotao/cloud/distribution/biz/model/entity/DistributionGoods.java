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
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 分销商品
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 14:59:18
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DistributionGoods.TABLE_NAME)
@TableName(DistributionGoods.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = DistributionGoods.TABLE_NAME, comment = "分销商品表")
public class DistributionGoods extends BaseSuperEntity<DistributionGoods, Long> {

    public static final String TABLE_NAME = "tt_distribution_goods";

    /** 商品ID */
    @Column(name = "goods_id", columnDefinition = "bigint not null  comment '商品ID'")
    private Long goodsId;
    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(255) not null  comment '商品名称'")
    private String goodsName;
    /** 规格ID */
    @Column(name = "sku_id", columnDefinition = "bigint not null  comment '规格ID'")
    private Long skuId;
    /** 规格信息json */
    @Column(name = "specs", columnDefinition = "json not null  comment '规格信息json'")
    private String specs;
    /** 店铺ID */
    @Column(name = "store_id", columnDefinition = "bigint not null  comment '店铺ID'")
    private Long storeId;
    /** 店铺名称 */
    @Column(name = "store_name", columnDefinition = "varchar(255) not null  comment '店铺名称'")
    private String storeName;
    /** 佣金金额 */
    @Column(name = "commission", columnDefinition = "decimal(10,2) not null  comment '佣金金额'")
    private BigDecimal commission;
    /** 商品价格 */
    @Column(name = "price", columnDefinition = "decimal(10,2) not null  comment '商品价格'")
    private BigDecimal price;
    /** 缩略图路径 */
    @Column(name = "thumbnail", columnDefinition = "varchar(255) not null  comment '缩略图路径'")
    private String thumbnail;
    /** 库存 */
    @Column(name = "quantity", columnDefinition = "int not null  comment '库存'")
    private Integer quantity;

    public DistributionGoods(GoodsSku goodsSku, BigDecimal commission) {
        this.goodsId = goodsSku.getGoodsId();
        this.goodsName = goodsSku.getGoodsName();
        this.skuId = goodsSku.getId();
        this.specs = "";
        JSONObject jsonObject = JSONUtil.parseObj(goodsSku.getSpecs());
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if (!"images".equals(entry.getKey())) {
                this.specs = this.specs + entry.getKey() + ":" + entry.getValue() + " ";
            }
        }

        this.storeId = goodsSku.getStoreId();
        this.storeName = goodsSku.getStoreName();
        this.price = goodsSku.getPrice();
        this.thumbnail = goodsSku.getThumbnail();
        this.quantity = goodsSku.getQuantity();
        this.commission = commission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DistributionGoods distributionGoods = (DistributionGoods) o;
        return getId() != null && Objects.equals(getId(), distributionGoods.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
