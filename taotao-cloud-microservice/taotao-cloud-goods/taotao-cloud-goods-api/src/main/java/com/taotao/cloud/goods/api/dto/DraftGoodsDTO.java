package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 草稿商品DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:15
 */
@Setter
@Getter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DraftGoodsDTO extends DraftGoodsBaseDTO {

	private static final long serialVersionUID = 5255666163196674178L;

	@Valid
	@Schema(description = "商品参数")
	private List<GoodsParamsDTO> goodsParamsDTOList;

	@Schema(description = "商品图片")
	private List<String> goodsGalleryList;

	@Valid
	@Schema(description = "sku列表")
	private List<Map<String, Object>> skuList;

}
