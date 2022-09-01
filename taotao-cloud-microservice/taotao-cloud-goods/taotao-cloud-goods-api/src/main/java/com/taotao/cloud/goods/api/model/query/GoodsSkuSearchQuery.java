package com.taotao.cloud.goods.api.model.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.lang.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;

/**
 * 规格商品查询条件
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuSearchQuery extends GoodsPageQuery {

	@Serial
	private static final long serialVersionUID = -6235885068610635045L;

	@Schema(description = "商品id")
	private String goodsId;

	@Override
	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = super.queryWrapper();
		queryWrapper.eq(StringUtils.isNotEmpty(goodsId), "goods_id", goodsId);
		return queryWrapper;
	}
}
