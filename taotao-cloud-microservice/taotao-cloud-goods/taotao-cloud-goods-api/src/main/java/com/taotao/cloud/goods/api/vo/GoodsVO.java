package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.goods.api.dto.GoodsParamsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 商品VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:32:40
 */
@Schema(description = "商品VO")
public record GoodsVO(
	@Schema(description = "分类名称")
	List<String> categoryName,

	@Schema(description = "商品参数")
	List<GoodsParamsDTO> goodsParamsDTOList,

	@Schema(description = "商品图片")
	List<String> goodsGalleryList,

	@Schema(description = "sku列表")
	List<GoodsSkuVO> skuList,

	GoodsBaseVO goodsBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 6377623919990713567L;

}
