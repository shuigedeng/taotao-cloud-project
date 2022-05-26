package com.taotao.cloud.goods.api.vo;

import com.taotao.cloud.common.model.PageModel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 库存警告封装类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:52:39
 */
public record StockWarningVO(

	@Schema(description = "库存警告数量")
	Integer stockWarningNum,

	@Schema(description = "商品SKU列表")
	PageModel<GoodsSkuBaseVO> goodsSkuPage
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
