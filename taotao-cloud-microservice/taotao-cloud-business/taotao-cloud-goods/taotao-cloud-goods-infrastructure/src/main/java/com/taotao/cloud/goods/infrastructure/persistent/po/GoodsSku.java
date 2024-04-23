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

package com.taotao.cloud.goods.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/** 商品sku表 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GoodsSku.TABLE_NAME)
@TableName(GoodsSku.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = GoodsSku.TABLE_NAME, comment = "商品sku表")
public class GoodsSku extends BaseSuperEntity<GoodsSku, Long> {

    public static final String TABLE_NAME = "tt_goods_sku";

    /** 商品id */
    @Column(name = "goods_id", columnDefinition = "bigint not null comment '商品id'")
    private Long goodsId;

    /** 规格信息json */
    @Column(name = "specs", columnDefinition = "json not null comment '规格信息json'")
    private String specs;

    /** 规格信息 */
    @Column(name = "simple_specs", columnDefinition = "mediumtext not null comment '规格信息'")
    private String simpleSpecs;

    /** 配送模版id */
    @Column(name = "freight_template_id", columnDefinition = "bigint not null comment '配送模版id'")
    private Long freightTemplateId;

    /** 是否是促销商品 */
    @Column(name = "promotion_flag", columnDefinition = "boolean not null comment '是否是促销商品'")
    private Boolean promotionFlag;

    /** 促销价 */
    @Column(name = "promotion_price", columnDefinition = "decimal(10,2) not null comment '促销价'")
    private BigDecimal promotionPrice;

    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(255) not null comment '商品名称'")
    private String goodsName;

    /** 商品编号 */
    @Column(name = "sn", columnDefinition = "varchar(255) not null comment '商品编号'")
    private String sn;

    /** 品牌id */
    @Column(name = "brand_id", columnDefinition = "bigint not null comment '品牌id'")
    private Long brandId;

    /** 分类path */
    @Column(name = "category_path", columnDefinition = "varchar(255) not null comment '分类path'")
    private String categoryPath;

    /** 计量单位 */
    @Column(name = "goods_unit", columnDefinition = "varchar(255) not null comment '计量单位'")
    private String goodsUnit;

    /** 卖点 */
    @Column(name = "selling_point", columnDefinition = "varchar(255) not null comment '卖点'")
    private String sellingPoint;

    /** 重量 */
    @Column(name = "weight", columnDefinition = "decimal(10,2) not null comment '重量'")
    private BigDecimal weight;

    /**
     * 上架状态
     *
     * @see GoodsStatusEnum
     */
    @Column(name = "market_enable", columnDefinition = "varchar(255) not null comment '上架状态'")
    private String marketEnable;

    /** 商品详情 */
    @Column(name = "intro", columnDefinition = "mediumtext not null comment '商品详情'")
    private String intro;

    /** 商品价格 */
    @Column(name = "price", columnDefinition = "decimal(10,2) not null comment '商品价格'")
    private BigDecimal price;

    /** 成本价格 */
    @Column(name = "cost", columnDefinition = "decimal(10,2) not null comment '成本价格'")
    private BigDecimal cost;

    /** 浏览数量 */
    @Column(name = "view_count", columnDefinition = "int not null default 0 comment '浏览数量'")
    private Integer viewCount;

    /** 购买数量 */
    @Column(name = "buy_count", columnDefinition = "int not null default 0 comment '购买数量'")
    private Integer buyCount;

    /** 库存 */
    @Column(name = "quantity", columnDefinition = "int not null default 0 comment '库存'")
    private Integer quantity;

    /** 商品好评率 */
    @Column(name = "grade", columnDefinition = "decimal(10,2) not null default  0 comment '商品好评率'")
    private BigDecimal grade;

    /** 缩略图路径 */
    @Column(name = "thumbnail", columnDefinition = "varchar(255) not null comment '缩略图路径'")
    private String thumbnail;

    /** 大图路径 */
    @Column(name = "big", columnDefinition = "varchar(255) not null comment '大图路径'")
    private String big;

    /** 小图路径 */
    @Column(name = "small", columnDefinition = "varchar(255) not null comment '小图路径'")
    private String small;

    /** 原图路径 */
    @Column(name = "original", columnDefinition = "varchar(255) not null comment '原图路径'")
    private String original;

    /** 店铺分类路径 */
    @Column(name = "store_category_path", columnDefinition = "varchar(255) not null comment '店铺分类路径'")
    private String storeCategoryPath;

    /** 评论数量 */
    @Column(name = "comment_num", columnDefinition = "int not null default 0 comment '评论数量'")
    private Integer commentNum;

    /** 卖家id */
    @Column(name = "store_id", columnDefinition = "bigint not null comment '卖家id'")
    private Long storeId;

    /** 卖家名字 */
    @Column(name = "store_name", columnDefinition = "varchar(255) not null comment '卖家名字'")
    private String storeName;

    /** 运费模板id */
    @Column(name = "template_id", columnDefinition = "bigint not null comment '运费模板id'")
    private Long templateId;

    /**
     * 审核状态
     *
     * @see GoodsAuthEnum
     */
    @Column(name = "auth_flag", columnDefinition = "varchar(255) not null comment '审核状态'")
    private String authFlag;

    /** 审核信息 */
    @Column(name = "auth_message", columnDefinition = "varchar(255) null comment '审核信息'")
    private String authMessage;

    /** 下架原因 */
    @Column(name = "under_message", columnDefinition = "varchar(255) null comment '下架原因'")
    private String underMessage;

    /** 是否自营 */
    @Column(name = "self_operated", columnDefinition = "boolean not null default false comment '是否自营'")
    private Boolean selfOperated;

    /** 商品移动端详情 */
    @Column(name = "mobile_intro", columnDefinition = "mediumtext not null comment '商品移动端详情'")
    private String mobileIntro;

    /** 商品视频 */
    @Column(name = "goods_video", columnDefinition = "varchar(255) not null comment '商品视频'")
    private String goodsVideo;

    /** 是否为推荐商品 */
    @Column(name = "recommend", columnDefinition = "boolean not null default false comment '是否为推荐商品'")
    private Boolean recommend;

    /** 销售模式 */
    @Column(name = "sales_model", columnDefinition = "varchar(255) not null comment '销售模式'")
    private String salesModel;

    /**
     * 商品类型
     *
     * @see GoodsTypeEnum
     */
    @Column(name = "goods_type", columnDefinition = "varchar(255) not null comment '商品类型'")
    private String goodsType;

    public BigDecimal getWeight() {
        if (weight == null) {
            return BigDecimal.ZERO;
        }
        return weight;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        if (super.getUpdateTime() == null) {
            return LocalDateTime.ofEpochSecond(1593571928, 0, ZoneOffset.of("+8"));
        } else {
            return super.getUpdateTime();
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
        GoodsSku goodsSku = (GoodsSku) o;
        return getId() != null && Objects.equals(getId(), goodsSku.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

	/**
	 * 设置规格商品的基本商品信息
	 *
	 * @param goods 基本商品信息
	 */
	// public GoodsSku(Goods goods) {
	// 	//商品基本信息
	// 	this.goodsId = goods.getId();
	// 	this.goodsName = goods.getGoodsName();
	// 	this.goodsType = goods.getGoodsType();
	//
	// 	this.selfOperated = goods.getSelfOperated();
	// 	this.sellingPoint = goods.getSellingPoint();
	// 	this.categoryPath = goods.getCategoryPath();
	// 	this.brandId = goods.getBrandId();
	// 	this.marketEnable = goods.getMarketEnable();
	// 	this.intro = goods.getIntro();
	// 	this.mobileIntro = goods.getMobileIntro();
	// 	this.goodsUnit = goods.getGoodsUnit();
	// 	this.grade = new BigDecimal(100);
	// 	//商品状态
	// 	this.authFlag = goods.getAuthFlag();
	// 	this.salesModel = goods.getSalesModel();
	// 	//卖家信息
	// 	this.storeId = goods.getStoreId();
	// 	this.storeName = goods.getStoreName();
	// 	this.storeCategoryPath = goods.getStoreCategoryPath();
	// 	this.freightTemplateId = goods.getTemplateId();
	// 	this.recommend = goods.getRecommend();
	// }
}
