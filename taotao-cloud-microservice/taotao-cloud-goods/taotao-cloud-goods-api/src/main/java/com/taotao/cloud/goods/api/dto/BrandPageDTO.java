package com.taotao.cloud.goods.api.dto;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品品牌dto
 */
@Data
@Schema(description = "商品品牌dto")
public class BrandPageDTO extends PageParam {

	private static final long serialVersionUID = 8906820486037326039L;

	@Schema(description = "品牌名称")
	private String name;
}
