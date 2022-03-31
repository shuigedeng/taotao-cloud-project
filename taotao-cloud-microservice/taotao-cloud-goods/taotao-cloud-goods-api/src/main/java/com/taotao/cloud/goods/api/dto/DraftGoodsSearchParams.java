package com.taotao.cloud.goods.api.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.goods.api.enums.DraftGoodsSaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 草稿商品搜索对象
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DraftGoodsSearchParams extends GoodsSearchParams {

	private static final long serialVersionUID = -1057830772267228050L;

	/**
	 * @see DraftGoodsSaveType
	 */
	@Schema(description = "草稿商品保存类型")
	private String saveType;

	@Override
	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = super.queryWrapper();
		if (StrUtil.isNotEmpty(saveType)) {
			queryWrapper.eq("save_type", saveType);
		}
		return queryWrapper;
	}
}
