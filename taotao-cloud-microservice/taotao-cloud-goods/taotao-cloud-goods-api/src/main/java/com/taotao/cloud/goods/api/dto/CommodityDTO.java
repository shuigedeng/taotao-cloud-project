package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 直播商品DTO 用于获取直播商品状态时使用
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:07
 */
@RecordBuilder
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommodityDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;
	
	@Schema(description = "商品ID")
	private Long goodsId;

	@Schema(description = "商品名称")
	private String name;

	@Schema(description = "url")
	private String url;

	@Schema(description = "审核状态")
	private Integer auditStatus;
}
