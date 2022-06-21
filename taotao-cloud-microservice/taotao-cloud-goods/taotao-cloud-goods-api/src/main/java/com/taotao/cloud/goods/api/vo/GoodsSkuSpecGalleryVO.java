package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 商品规格VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:34:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuSpecGalleryVO extends GoodsSkuVO {

	@Serial
	private static final long serialVersionUID = -7651149660489332344L;

	@Schema(description = "规格列表")
	private List<SpecValueVO> specs;

	@Schema(description = "商品图片")
	private List<String> goodsGallerys;
}

