package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 直播商品DTO 用于获取直播商品状态时使用
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:07
 */
@Schema(description = "直播商品")
public record CommodityDTO(
	@Schema(description = "商品ID")
	Long goodsId,

	@Schema(description = "商品名称")
	String name,

	@Schema(description = "url")
	String url,

	@Schema(description = "审核状态")
	Integer auditStatus
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


}
