package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 商品品牌dto
 */
@Data
@Schema(description = "商品品牌dto")
public class BrandPageDTO {

	private static final long serialVersionUID = 8906820486037326039L;

	@Schema(description = "品牌名称")
	private String name;

	/**
	 * 当前第几页
	 */
	@Schema(description = "当前第几页，默认1", example = "1", required = true)
	@NotNull(message = "当前页显示数量不能为空")
	@Min(value = 0)
	@Max(value = Integer.MAX_VALUE)
	Integer currentPage;

	/**
	 * 每页显示条数
	 */
	@Schema(description = "每页显示条数，默认10", example = "10", required = true)
	@NotNull(message = "每页数据显示数量不能为空")
	@Min(value = 5)
	@Max(value = 100)
	Integer pageSize;
}
