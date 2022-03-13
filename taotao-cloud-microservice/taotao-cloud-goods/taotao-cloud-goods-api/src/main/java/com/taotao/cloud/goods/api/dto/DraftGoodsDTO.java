package com.taotao.cloud.goods.api.dto;

import cn.lili.modules.goods.entity.dos.DraftGoods;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.Data;

/**
 * 草稿商品DTO
 **/
@Data
public class DraftGoodsDTO extends DraftGoods {

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
