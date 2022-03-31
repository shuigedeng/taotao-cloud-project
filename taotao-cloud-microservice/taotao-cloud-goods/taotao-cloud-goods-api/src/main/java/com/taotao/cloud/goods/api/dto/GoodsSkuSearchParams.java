package com.taotao.cloud.goods.api.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.lang.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规格商品查询条件
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuSearchParams extends GoodsSearchParams {

	private static final long serialVersionUID = -6235885068610635045L;

	@Schema(description = "商品id")
	private String goodsId;

	@Override
	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = super.queryWrapper();
		queryWrapper.eq(StringUtil.isNotEmpty(goodsId), "goods_id", goodsId);
		return queryWrapper;
	}


}
