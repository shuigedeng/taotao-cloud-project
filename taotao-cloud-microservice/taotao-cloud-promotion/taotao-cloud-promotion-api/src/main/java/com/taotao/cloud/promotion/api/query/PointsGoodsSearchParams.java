package com.taotao.cloud.promotion.api.query;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 积分商品查询通用类
 **/
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsSearchParams extends BasePromotionsSearchParams {

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品skuId")
	private String skuId;

	@Schema(description = "积分商品分类编号")
	private String pointsGoodsCategoryId;

	@Schema(description = "积分,可以为范围，如10_1000")
	private String points;


	@Override
	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = super.queryWrapper();
		if (CharSequenceUtil.isNotEmpty(goodsName)) {
			queryWrapper.like("goods_name", goodsName);
		}
		if (CharSequenceUtil.isNotEmpty(skuId)) {
			queryWrapper.eq("sku_id", skuId);
		}
		if (CharSequenceUtil.isNotEmpty(pointsGoodsCategoryId)) {
			queryWrapper.eq("points_goods_category_id", pointsGoodsCategoryId);
		}
		if (CharSequenceUtil.isNotEmpty(points)) {
			String[] s = points.split("_");
			if (s.length > 1) {
				queryWrapper.between("points", s[0], s[1]);
			} else {
				queryWrapper.eq("points", s[0]);
			}
		}
		return queryWrapper;
	}

}
