package com.taotao.cloud.distribution.api.dto;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分销员商品查询条件
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销员商品查询条件")
public class DistributionGoodsSearchParams extends PageParam {

	@Schema(description = "商品ID")
	private String goodsId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "是否已选择")
	private boolean isChecked;

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = this.distributionQueryWrapper();
		queryWrapper.eq(CharSequenceUtil.isNotEmpty(goodsId), "goods_id", goodsId);
		queryWrapper.eq(CharSequenceUtil.isNotEmpty(goodsName), "goods_name", goodsId);
		return queryWrapper;
	}

	public <T> QueryWrapper<T> storeQueryWrapper() {
		QueryWrapper<T> queryWrapper = this.distributionQueryWrapper();
		queryWrapper.eq("dg.store_id", SecurityUtil.getUser().getStoreId());
		return queryWrapper;
	}

	public <T> QueryWrapper<T> distributionQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(CharSequenceUtil.isNotEmpty(goodsName), "dg.goods_name", goodsName);
		return queryWrapper;
	}

}
