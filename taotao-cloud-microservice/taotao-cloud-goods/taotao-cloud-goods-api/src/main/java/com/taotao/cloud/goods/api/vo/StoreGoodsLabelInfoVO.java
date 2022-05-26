package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * StoreGoodsLabelVO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:52:23
 */
@RecordBuilder
public record StoreGoodsLabelInfoVO(
	@Schema(description = "店铺商品分类ID")
	Long id,

	@Schema(description = "店铺商品分类名称")
	String labelName,

	@Schema(description = "层级, 从0开始")
	Integer level,

	@Schema(description = "店铺商品分类排序")
	Integer sortOrder,

	@Schema(description = "父id, 根节点为0")
	Long parentId,

	@Schema(description = "店铺ID")
	Long storeId
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
