package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 直播商品VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:46
 */
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommodityVO extends CommodityBaseVO {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

	@Schema(description = "SKU库存")
	private Integer quantity;

	@Schema(description = "店铺名称")
	private String storeName;
}
