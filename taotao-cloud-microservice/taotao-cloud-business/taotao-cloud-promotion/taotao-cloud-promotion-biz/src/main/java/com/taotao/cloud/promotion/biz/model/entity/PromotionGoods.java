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
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityGoodsDTO;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
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
 * 促销活动商品实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:32
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = PromotionGoods.TABLE_NAME)
@TableName(PromotionGoods.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = PromotionGoods.TABLE_NAME, comment = "促销商品")
public class PromotionGoods extends BaseSuperEntity<PromotionGoods, Long> {

    public static final String TABLE_NAME = "tt_promotion_goods";
    /** 商家ID */
    @Column(name = "store_id", columnDefinition = "bigint not null  comment '商家ID'")
    private Long storeId;
    /** 商家名称 */
    @Column(name = "store_name", columnDefinition = "varchar(255) not null  comment '商家名称'")
    private String storeName;
    /** 商品id */
    @Column(name = "goods_id", columnDefinition = "bigint not null  comment '商品id'")
    private Long goodsId;
    /** 商品SkuId */
    @Column(name = "sku_id", columnDefinition = "bigint not null  comment '商品SkuId'")
    private Long skuId;
    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(255) not null  comment '商品名称'")
    private String goodsName;
    /** 缩略图 */
    @Column(name = "thumbnail", columnDefinition = "varchar(255) not null  comment '缩略图'")
    private String thumbnail;
    /** 活动开始时间 */
    @Column(name = "start_time", columnDefinition = "datetime not null  comment '活动开始时间'")
    private LocalDateTime startTime;
    /** 活动结束时间 */
    @Column(name = "end_time", columnDefinition = "datetime not null  comment '活动结束时间'")
    private LocalDateTime endTime;
    /** 活动id */
    @Column(name = "promotion_id", columnDefinition = "bigint not null  comment '活动id'")
    private Long promotionId;

    /**
     * 促销工具类型
     *
     * @see PromotionTypeEnum
     */
    @Column(name = "promotion_type", columnDefinition = "varchar(255) not null  comment '促销工具类型'")
    private String promotionType;

    /**
     * 商品类型
     *
     * @see GoodsTypeEnum
     */
    @Column(name = "goods_type", columnDefinition = "varchar(255) not null  comment '商品类型'")
    private String goodsType;
    /** 活动标题 */
    @Column(name = "title", columnDefinition = "varchar(255) not null  comment '活动标题'")
    private String title;
    /** 卖出的商品数量 */
    @Column(name = "num", columnDefinition = "int not null  comment '卖出的商品数量'")
    private Integer num;
    /** 原价 */
    @Column(name = "original_price", columnDefinition = "decimal(10,2) not null  comment '原价'")
    private BigDecimal originalPrice;
    /** 促销价格 */
    @Column(name = "price", columnDefinition = "decimal(10,2) not null  comment '促销价格'")
    private BigDecimal price;
    /** 兑换积分 */
    @Column(name = "points", columnDefinition = "bigint not null  comment '兑换积分'")
    private Long points;
    /** 限购数量 */
    @Column(name = "limit_num", columnDefinition = "int not null  comment '限购数量'")
    private Integer limitNum;
    /** 促销库存 */
    @Column(name = "quantity", columnDefinition = "int not null  comment '促销库存'")
    private Integer quantity;
    /** 分类path */
    @Column(name = "category_path", columnDefinition = "varchar(255) not null  comment '分类path'")
    private String categoryPath;

    /**
     * 关联范围类型
     *
     * @see PromotionsScopeTypeEnum
     */
    @Column(name = "scope_type", columnDefinition = "varchar(255) not null  comment '关联范围类型'")
    private String scopeType = PromotionsScopeTypeEnum.PORTION_GOODS.name();

    /** 范围关联的id */
    @Column(name = "scope_id", columnDefinition = "bigint not null  comment '范围关联的id'")
    private Long scopeId;

    public PromotionGoods(GoodsSku sku) {
        if (sku != null) {
            BeanUtil.copyProperties(sku, this, "id", "price");
            this.skuId = sku.getId();
            this.originalPrice = sku.getPrice();
        }
    }

    public PromotionGoods(PointsGoods pointsGoods, GoodsSku sku) {
        if (pointsGoods != null) {
            BeanUtil.copyProperties(sku, this, "id");
            BeanUtil.copyProperties(pointsGoods, this, "id");
            this.promotionId = pointsGoods.getId();
            this.quantity = pointsGoods.getActiveStock();
            this.originalPrice = sku.getPrice();
        }
    }

    public PromotionGoods(KanjiaActivityGoodsDTO kanjiaActivityGoodsDTO) {
        if (kanjiaActivityGoodsDTO != null) {
            BeanUtil.copyProperties(kanjiaActivityGoodsDTO, this, "id");
            BeanUtil.copyProperties(kanjiaActivityGoodsDTO.getGoodsSku(), this, "id");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        PromotionGoods promotionGoods = (PromotionGoods) o;
        return getId() != null && Objects.equals(getId(), promotionGoods.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
