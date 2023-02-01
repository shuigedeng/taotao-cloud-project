package com.taotao.cloud.goods.api.model.page;

import com.taotao.cloud.goods.api.enums.DraftGoodsSaveTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 草稿商品搜索对象
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 22:07:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DraftGoodsPageQuery extends GoodsPageQuery {

	@Serial
	private static final long serialVersionUID = -1057830772267228050L;

	/**
	 * @see DraftGoodsSaveTypeEnum
	 */
	@Schema(description = "草稿商品保存类型")
	private String saveType;


}
