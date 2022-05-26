package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

/**
 * 草稿商品DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:15
 */
public record DraftGoodsDTO(
	@Valid
	@Schema(description = "商品参数")
	List<GoodsParamsDTO> goodsParamsDTOList,

	@Schema(description = "商品图片")
	List<String> goodsGalleryList,

	@Valid
	@Schema(description = "sku列表")
	List<Map<String, Object>> skuList,

	DraftGoodsBaseDTO draftGoodsBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 5255666163196674178L;


}
