package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 商品规格VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:34:20
 */
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuVO extends GoodsSkuBaseVO {

	@Serial
	private static final long serialVersionUID = -7651149660489332344L;

	@Schema(description = "规格列表")
	private List<SpecValueVO> specList;

	@Schema(description = "商品图片")
	private List<String> goodsGalleryList;
}

