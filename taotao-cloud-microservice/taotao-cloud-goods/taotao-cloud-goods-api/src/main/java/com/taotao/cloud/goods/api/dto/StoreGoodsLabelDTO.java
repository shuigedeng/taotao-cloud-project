package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * StoreGoodsLabelVO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:53
 */
@Schema(description = "店铺信息DTO")
public record StoreGoodsLabelDTO(
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
