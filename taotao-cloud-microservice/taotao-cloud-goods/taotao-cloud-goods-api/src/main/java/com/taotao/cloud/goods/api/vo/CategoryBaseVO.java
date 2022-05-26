package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品分类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:33
 */
@RecordBuilder
@Schema(description = "商品分类VO")
public record CategoryBaseVO(
	@Schema(description = "id")
	Long id,

	@Schema(description = "分类名称")
	String name,

	@Schema(description = " 父id, 根节点为0")
	Long parentId,

	@Schema(description = "层级, 从0开始")
	Integer level,

	@Schema(description = "排序值")
	Integer sortOrder,

	@Schema(description = "佣金比例")
	BigDecimal commissionRate,

	@Schema(description = "分类图标")
	String image,

	@Schema(description = "是否支持频道")
	Boolean supportChannel
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;


}
