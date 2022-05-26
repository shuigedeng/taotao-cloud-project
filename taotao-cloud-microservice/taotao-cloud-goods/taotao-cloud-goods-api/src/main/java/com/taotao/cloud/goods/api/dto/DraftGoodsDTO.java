package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 草稿商品DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:15
 */
@RecordBuilder
public record DraftGoodsDTO(

	@Valid
	@Schema(description = "商品参数")
	List<GoodsParamsDTO> goodsParamsDTOList,

	@Schema(description = "商品图片")
	List<String> goodsGalleryList,

	@Valid
	@Schema(description = "sku列表")
	List<Map<String, Object>> skuList,

	@Valid
	@Schema(description = "草稿商品")
	@NotNull(message = "草稿商品信息不能为空")
	DraftGoodsBaseDTO draftGoodsBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 5255666163196674178L;


}
