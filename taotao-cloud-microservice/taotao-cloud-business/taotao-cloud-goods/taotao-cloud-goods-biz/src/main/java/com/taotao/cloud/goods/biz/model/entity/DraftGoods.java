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

package com.taotao.cloud.goods.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.goods.api.enums.DraftGoodsSaveTypeEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/** 草稿商品表 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Entity
@Table(name = DraftGoods.TABLE_NAME)
@TableName(DraftGoods.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = DraftGoods.TABLE_NAME)
public class DraftGoods extends BaseSuperEntity<DraftGoods, Long> {

    public static final String TABLE_NAME = "tt_draft_goods";

    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(255) not null comment '商品名称'")
    private String goodsName;

    /** 商品价格 */
    @Column(name = "price", columnDefinition = "decimal(10,2) not null comment '商品价格'")
    private BigDecimal price;

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

    /**
     * 上架状态
     *
     * @see GoodsStatusEnum
     */
    @Column(name = "market_enable", columnDefinition = "varchar(255) not null comment '上架状态'")
    private String marketEnable;

    /** 详情 */
    @Column(name = "intro", columnDefinition = "mediumtext not null comment '详情'")
    private String intro;

    /** 商品移动端详情 */
    @Column(name = "mobile_intro", columnDefinition = "mediumtext not null comment '商品移动端详情'")
    private String mobileIntro;

    /** 购买数量 */
    @Column(name = "buy_count", columnDefinition = "int null default 0 comment '购买数量'")
    private Integer buyCount;

    /** 库存 */
    @Column(name = "quantity", columnDefinition = "int not null default 0 comment '库存'")
    private Integer quantity;

    /** 可用库存 */
    @Column(name = "enable_quantity", columnDefinition = "int not null default 0 comment '可用库存'")
    private Integer enableQuantity;

    /** 商品好评率 */
    @Column(name = "grade", columnDefinition = "decimal(10,2) not null comment '商品好评率'")
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
    @Column(name = "comment_num", columnDefinition = "int default 0 comment '评论数量'")
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

    /** 是否自营 */
    @Column(name = "self_operated", columnDefinition = "boolean null default false comment '是否自营'")
    private Boolean selfOperated;

    /** 商品视频 */
    @Column(name = "goods_video", columnDefinition = "varchar(255) not null comment '商品视频'")
    private String goodsVideo;

    /** 是否为推荐商品 */
    @Column(name = "recommend", columnDefinition = "boolean null default false comment '是否为推荐商品'")
    private Boolean recommend;

    /** 销售模式 */
    @Column(name = "sales_model", columnDefinition = "varchar(255) not null comment '销售模式'")
    private String salesModel;

    /**
     * 草稿商品保存类型
     *
     * @see DraftGoodsSaveTypeEnum
     */
    @Column(name = "save_type", columnDefinition = "varchar(255) not null comment '草稿商品保存类型'")
    private String saveType;

    /** 分类名称JSON */
    @Column(name = "category_name_json", columnDefinition = "json not null comment '分类名称JSON'")
    private String categoryNameJson;

    /** 商品参数JSON */
    @Column(name = "goods_params_list_json", columnDefinition = "json not null comment '商品参数JSON'")
    private String goodsParamsListJson;

    /** 商品图片JSON */
    @Column(name = "goods_gallery_list_json", columnDefinition = "json not null comment '商品图片JSON'")
    private String goodsGalleryListJson;

    /** sku列表JSON */
    @Column(name = "sku_list_json", columnDefinition = "json not null comment 'sku列表JSON'")
    private String skuListJson;

    /**
     * 商品类型
     *
     * @see GoodsTypeEnum
     */
    @Column(name = "goods_type", columnDefinition = "varchar(255) not null comment '商品类型'")
    private String goodsType;

    public String getIntro() {
        if (CharSequenceUtil.isNotEmpty(intro)) {
            return HtmlUtil.unescape(intro);
        }
        return intro;
    }

    public String getMobileIntro() {
        if (CharSequenceUtil.isNotEmpty(mobileIntro)) {
            return HtmlUtil.unescape(mobileIntro);
        }
        return mobileIntro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DraftGoods that = (DraftGoods) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
