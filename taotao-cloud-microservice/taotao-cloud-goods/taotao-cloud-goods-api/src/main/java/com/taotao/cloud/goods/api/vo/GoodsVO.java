package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.goods.api.dto.GoodsParamsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 商品VO
 */
@Data
public class GoodsVO extends Goods {

	private static final long serialVersionUID = 6377623919990713567L;

	@Schema(description = "分类名称")
	private List<String> categoryName;

	@Schema(description = "商品参数")
	private List<GoodsParamsDTO> goodsParamsDTOList;

	@Schema(description = "商品图片")
	private List<String> goodsGalleryList;

	@Schema(description = "sku列表")
	private List<GoodsSkuVO> skuList;
}
