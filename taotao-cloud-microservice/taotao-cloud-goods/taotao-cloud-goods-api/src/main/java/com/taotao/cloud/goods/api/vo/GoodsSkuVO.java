package com.taotao.cloud.goods.api.vo;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品规格VO
 */
@Data
public class GoodsSkuVO extends GoodsSkuBaseVO {

	private static final long serialVersionUID = -7651149660489332344L;

	@Schema(description = "规格列表")
	private List<SpecValueVO> specList;

	@Schema(description = "商品图片")
	private List<String> goodsGalleryList;
}

