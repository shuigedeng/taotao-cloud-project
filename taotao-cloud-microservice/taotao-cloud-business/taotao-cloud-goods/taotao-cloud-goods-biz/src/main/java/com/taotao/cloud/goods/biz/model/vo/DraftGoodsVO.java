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

package com.taotao.cloud.goods.biz.model.vo;

import com.taotao.cloud.goods.api.enums.DraftGoodsSaveTypeEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 草稿商品基础VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 22:10:24
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DraftGoodsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1450550797436233753L;

    private Long id;

    /** 商品名称 */
    private String goodsName;

    /** 商品价格 */
    private BigDecimal price;

    /** 品牌id */
    private Long brandId;

    /** 分类path */
    private String categoryPath;

    /** 计量单位 */
    private String goodsUnit;

    /** 卖点 */
    private String sellingPoint;

    /**
     * 上架状态
     *
     * @see GoodsStatusEnum
     */
    private String marketEnable;

    /** 详情 */
    private String intro;

    /** 商品移动端详情 */
    private String mobileIntro;

    /** 购买数量 */
    private Integer buyCount;

    /** 库存 */
    private Integer quantity;

    /** 可用库存 */
    private Integer enableQuantity;

    /** 商品好评率 */
    private BigDecimal grade;

    /** 缩略图路径 */
    private String thumbnail;

    /** 大图路径 */
    private String big;

    /** 小图路径 */
    private String small;

    /** 原图路径 */
    private String original;

    /** 店铺分类路径 */
    private String storeCategoryPath;

    /** 评论数量 */
    private Integer commentNum;

    /** 卖家id */
    private Long storeId;

    /** 卖家名字 */
    private String storeName;

    /** 运费模板id */
    private Long templateId;

    /** 是否自营 */
    private Boolean selfOperated;

    /** 商品视频 */
    private String goodsVideo;

    /** 是否为推荐商品 */
    private Boolean recommend;

    /** 销售模式 */
    private String salesModel;

    /**
     * 草稿商品保存类型
     *
     * @see DraftGoodsSaveTypeEnum
     */
    private String saveType;

    /** 分类名称JSON */
    private String categoryNameJson;

    /** 商品参数JSON */
    private String goodsParamsListJson;

    /** 商品图片JSON */
    private String goodsGalleryListJson;

    /** sku列表JSON */
    private String skuListJson;

    /**
     * 商品类型
     *
     * @see GoodsTypeEnum
     */
    private String goodsType;
}
