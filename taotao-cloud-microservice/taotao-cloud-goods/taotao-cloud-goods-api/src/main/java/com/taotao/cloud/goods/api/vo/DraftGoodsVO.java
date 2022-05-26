package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.goods.api.dto.GoodsParamsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 草稿商品VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 15:59:38
 */
public record DraftGoodsVO(
	@Schema(description = "分类名称")
	List<String> categoryName,

	@Schema(description = "商品参数")
	List<GoodsParamsDTO> goodsParamsDTOList,

	@Schema(description = "商品图片")
	List<String> goodsGalleryList,

	@Schema(description = "sku列表")
	List<GoodsSkuVO> skuList,

	@Schema(description = "草稿商品")
	DraftGoodsBaseVO draftGoodsBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 6377623919990713567L;

}
