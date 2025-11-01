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

package com.taotao.cloud.goods.biz.elasticsearch.entity;

import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.cloud.goods.api.enums.GoodsTypeEnum;
import com.taotao.cloud.goods.biz.model.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.biz.elasticsearch.pojo.EsGoodsAttribute;
import com.taotao.cloud.goods.biz.elasticsearch.pojo.EsSuffix;
import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;
import lombok.experimental.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 商品索引
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:18:08
 */
@Data
@Document(indexName = "#{@elasticsearchProperties.indexPrefix}_" + EsSuffix.GOODS_INDEX_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
@Accessors(chain = true)
public class EsGoodsIndex implements Serializable {

	@Serial
	private static final long serialVersionUID = -6856471777036048874L;

	@Id
	private Long id;

	/**
	 * 商品id
	 */
	@Field(type = FieldType.Long)
	private Long goodsId;

	/**
	 * 商品名称
	 */
	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String goodsName;

	/**
	 * 商品编号
	 */
	@Field(type = FieldType.Keyword)
	private String sn;

	/**
	 * 卖家id
	 */
	@Field(type = FieldType.Long)
	private Long storeId;

	/**
	 * 卖家名称
	 */
	@Field(type = FieldType.Text)
	private String storeName;

	/**
	 * 销量
	 */
	@Field(type = FieldType.Integer)
	private Integer buyCount;

	/**
	 * 小图
	 */
	private String small;

	/**
	 * 缩略图
	 */
	private String thumbnail;

	/**
	 * 品牌id
	 */
	@Field(type = FieldType.Long, fielddata = true)
	private Long brandId;

	/**
	 * 品牌名称
	 */
	@Field(type = FieldType.Keyword, fielddata = true)
	private String brandName;

	/**
	 * 品牌图片地址
	 */
	@Field(type = FieldType.Keyword, fielddata = true)
	private String brandUrl;

	/**
	 * 分类path
	 */
	@Field(type = FieldType.Keyword)
	private String categoryPath;

	/**
	 * 分类名称path
	 */
	@Field(type = FieldType.Keyword)
	private String categoryNamePath;

	/**
	 * 店铺分类id
	 */
	@Field(type = FieldType.Keyword)
	private String storeCategoryPath;

	/**
	 * 店铺分类名称
	 */
	@Field(type = FieldType.Keyword)
	private String storeCategoryNamePath;

	/**
	 * 商品价格
	 */
	@Field(type = FieldType.Double)
	private BigDecimal price;

	/**
	 * 促销价
	 */
	@Field(type = FieldType.Double)
	private BigDecimal promotionPrice;

	/**
	 * 如果是积分商品需要使用的积分
	 */
	@Field(type = FieldType.Integer)
	private Integer point;

	/**
	 * 评价数量
	 */
	@Field(type = FieldType.Integer)
	private Integer commentNum;

	/**
	 * 好评数量
	 */
	@Field(type = FieldType.Integer)
	private Integer highPraiseNum;

	/**
	 * 好评率
	 */
	@Field(type = FieldType.Double)
	private BigDecimal grade;

	/**
	 * 详情
	 */
	@Field(type = FieldType.Text)
	private String intro;

	/**
	 * 商品移动端详情
	 */
	@Field(type = FieldType.Text)
	private String mobileIntro;

	/**
	 * 是否自营
	 */
	@Field(type = FieldType.Boolean)
	private Boolean selfOperated;

	/**
	 * 是否为推荐商品
	 */
	@Field(type = FieldType.Boolean)
	private Boolean recommend;

	/**
	 * 销售模式
	 */
	@Field(type = FieldType.Text)
	private String salesModel;

	/**
	 * 审核状态
	 */
	@Field(type = FieldType.Text)
	private String authFlag;

	/**
	 * 卖点
	 */
	@Field(type = FieldType.Text)
	private String sellingPoint;

	/**
	 * 上架状态
	 */
	@Field(type = FieldType.Text)
	private String marketEnable;

	/**
	 * 商品视频
	 */
	@Field(type = FieldType.Text)
	private String goodsVideo;

	@Field(type = FieldType.Date, format = DateFormat.basic_date_time)
	private LocalDateTime releaseTime;

	/**
	 * 商品类型
	 *
	 * @see GoodsTypeEnum
	 */
	private String goodsType;

	/**
	 * 商品sku基础分数
	 */
	private Integer skuSource;

	/**
	 * 商品属性（参数和规格）
	 */
	@Field(type = FieldType.Nested)
	private List<EsGoodsAttribute> attrList;

	/**
	 * 商品促销活动集合JSON，key 为 促销活动类型，value 为 促销活动实体信息
	 *
	 * @see PromotionTypeEnum value 为 促销活动实体信息
	 */
	@Field(type = FieldType.Nested)
	private String promotionMapJson;

	public EsGoodsIndex(GoodsSku sku) {
		if (sku != null) {
			this.id = sku.getId();
			this.goodsId = sku.getGoodsId();
			this.goodsName = sku.getGoodsName();
			this.price = sku.getPrice();
			this.storeName = sku.getStoreName();
			this.storeId = sku.getStoreId();
			this.thumbnail = sku.getThumbnail();
			this.categoryPath = sku.getCategoryPath();
			this.goodsVideo = sku.getGoodsVideo();
			this.mobileIntro = sku.getMobileIntro();
			this.buyCount = sku.getBuyCount() != null ? sku.getBuyCount() : 0;
			this.commentNum = sku.getCommentNum();
			this.small = sku.getSmall();
			this.brandId = sku.getBrandId();
			this.sn = sku.getSn();
			this.storeCategoryPath = sku.getStoreCategoryPath();
			this.sellingPoint = sku.getSellingPoint();
			this.selfOperated = sku.getSelfOperated();
			this.salesModel = sku.getSalesModel();
			this.marketEnable = sku.getMarketEnable();
			this.authFlag = sku.getAuthFlag();
			this.intro = sku.getIntro();
			this.grade = sku.getGrade();
			this.recommend = sku.getRecommend();
			this.goodsType = sku.getGoodsType();
			this.releaseTime = LocalDateTime.now();
		}
	}

	/**
	 * 参数索引增加
	 *
	 * @param sku            商品sku信息
	 * @param goodsParamDTOS 商品参数信息
	 */
	public EsGoodsIndex(GoodsSku sku, List<GoodsParamsDTO> goodsParamDTOS) {
		this(sku);
		// 如果参数不为空
		if (goodsParamDTOS != null && !goodsParamDTOS.isEmpty()) {
			// 接受不了参数索引
			List<EsGoodsAttribute> attributes = new ArrayList<>();
			// 循环参数分组
			goodsParamDTOS.forEach(goodsParamGroup -> {
				// 如果参数有配置，则增加索引
				if (goodsParamGroup.getGoodsParamsItemDTOList() != null
					&& !goodsParamGroup.getGoodsParamsItemDTOList().isEmpty()) {
					// 循环分组的内容
					goodsParamGroup.getGoodsParamsItemDTOList().forEach(goodsParam -> {
						// 如果字段需要索引，则增加索引字段
						if (goodsParam.getIsIndex() != null && goodsParam.getIsIndex() == 1) {
							EsGoodsAttribute attribute = new EsGoodsAttribute();
							attribute.setType(1);
							attribute.setName(goodsParam.getParamName());
							attribute.setValue(goodsParam.getParamValue());
							attribute.setSort(goodsParam.getSort());
							attributes.add(attribute);
						}
					});
				}
			});
			this.attrList = attributes;
		}
	}

	public void setGoodsSku(GoodsSku sku) {
		if (sku != null) {
			this.id = sku.getId();
			this.goodsId = sku.getGoodsId();
			this.goodsName = sku.getGoodsName();
			this.price = sku.getPrice();
			this.storeName = sku.getStoreName();
			this.storeId = sku.getStoreId();
			this.thumbnail = sku.getThumbnail();
			this.categoryPath = sku.getCategoryPath();
			this.goodsVideo = sku.getGoodsVideo();
			this.mobileIntro = sku.getMobileIntro();
			this.buyCount = sku.getBuyCount();
			this.commentNum = sku.getCommentNum();
			this.small = sku.getSmall();
			this.brandId = sku.getBrandId();
			this.sn = sku.getSn();
			this.storeCategoryPath = sku.getStoreCategoryPath();
			this.sellingPoint = sku.getSellingPoint();
			this.selfOperated = sku.getSelfOperated();
			this.salesModel = sku.getSalesModel();
			this.marketEnable = sku.getMarketEnable();
			this.authFlag = sku.getAuthFlag();
			this.intro = sku.getIntro();
			this.grade = sku.getGrade();
			this.releaseTime = LocalDateTime.now();
		}
	}

	public Map<String, Object> getPromotionMap() {
		return PromotionTools.filterInvalidPromotionsMap(JSONUtil.parseObj(this.promotionMapJson));
	}
}
