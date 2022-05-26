package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 直播商品VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:46
 */
public record CommodityVO(

	@Schema(description = "SKU库存")
	Integer quantity,

	@Schema(description = "店铺名称")
	String storeName,

	@Schema(description = "店铺名称")
	CommodityBaseVO commodityBase
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

}
