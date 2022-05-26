package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 商品计量VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:06:43
 */
@RecordBuilder
@Schema(description = "商品计量VO")
public record GoodsUnitVO(

	Long id,

	@Schema(description = "计量单位名称")
	String name
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -4433579132929428572L;

}
