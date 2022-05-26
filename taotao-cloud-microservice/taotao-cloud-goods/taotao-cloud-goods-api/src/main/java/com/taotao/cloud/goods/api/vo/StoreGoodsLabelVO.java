package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * StoreGoodsLabelVO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:52:23
 */
public record StoreGoodsLabelVO(
	@Schema(description = "店铺商品分类ID")
	Long id,

	@Schema(description = "店铺商品分类名称")
	String labelName,

	@Schema(description = "层级, 从0开始")
	Integer level,

	@Schema(description = "店铺商品分类排序")
	Integer sortOrder,

	@Schema(description = "下级分类列表")
	List<StoreGoodsLabelVO> children

) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	//public StoreGoodsLabelVO(Long id, String labelName, Integer level, Integer sortOrder) {
	//	this.id = id;
	//	this.labelName = labelName;
	//	this.level = level;
	//	this.sortOrder = sortOrder;
	//}
}
