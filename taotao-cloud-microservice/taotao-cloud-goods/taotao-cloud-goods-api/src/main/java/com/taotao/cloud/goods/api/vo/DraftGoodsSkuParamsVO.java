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
 * 草稿商品VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 15:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DraftGoodsSkuParamsVO extends DraftGoodsVO {

	@Serial
	private static final long serialVersionUID = 6377623919990713567L;

	@Schema(description = "分类名称")
	private List<String> categoryName;

	@Schema(description = "商品参数")
	private List<GoodsParamsVO> goodsParams;

	@Schema(description = "商品图片")
	private List<String> goodsGallerys;

	@Schema(description = "sku列表")
	private List<GoodsSkuVO> skus;
}
