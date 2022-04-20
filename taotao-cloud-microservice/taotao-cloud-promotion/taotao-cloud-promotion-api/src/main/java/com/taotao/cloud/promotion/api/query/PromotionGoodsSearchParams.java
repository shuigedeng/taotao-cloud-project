package com.taotao.cloud.promotion.api.query;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * 促销商品查询通用类
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionGoodsSearchParams extends BasePromotionsSearchParams {

	@Schema(description = "促销活动id")
	private String promotionId;

	@Schema(description = "促销类型")
	private String promotionType;

	@Schema(description = "商品活动id")
	private String storeId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品分类路径")
	private String categoryPath;

	@Schema(description = "商品SkuId")
	private String skuId;

	@Schema(description = "商品SkuIds")
	private List<String> skuIds;

	@Schema(description = "促销活动id")
	private List<String> promotionIds;


	@Override
	public <T> QueryWrapper<T> queryWrapper() {
		if (CharSequenceUtil.isEmpty(this.getScopeType())) {
			this.setScopeType(PromotionsScopeTypeEnum.PORTION_GOODS.name());
		}
		QueryWrapper<T> queryWrapper = super.queryWrapper();
		if (CharSequenceUtil.isNotEmpty(promotionId)) {
			queryWrapper.eq("promotion_id", promotionId);
		}
		if (CharSequenceUtil.isNotEmpty(goodsName)) {
			queryWrapper.like("goods_name", goodsName);
		}
		if (CharSequenceUtil.isNotEmpty(promotionType)) {
			queryWrapper.eq("promotion_type", promotionType);
		}
		if (CharSequenceUtil.isNotEmpty(categoryPath)) {
			queryWrapper.like("category_path", categoryPath);
		}
		if (CharSequenceUtil.isNotEmpty(storeId)) {
			queryWrapper.in("store_id", Arrays.asList(storeId.split(",")));
		}
		if (CharSequenceUtil.isNotEmpty(skuId)) {
			queryWrapper.in("sku_id", Arrays.asList(skuId.split(",")));
		}
		if (skuIds != null && !skuIds.isEmpty()) {
			queryWrapper.in("sku_id", skuIds);
		}
		if (promotionIds != null && promotionIds.isEmpty()) {
			queryWrapper.in("promotion_id", promotionIds);
		}
		return queryWrapper;
	}

}
