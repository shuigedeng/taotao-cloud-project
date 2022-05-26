package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 商品规格VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:34:20
 */
public record GoodsSkuVO(

	@Schema(description = "规格列表")
	List<SpecValueVO> specList,

	@Schema(description = "商品图片")
	List<String> goodsGalleryList,

	GoodsSkuBaseVO goodsSkuBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7651149660489332344L;

}

