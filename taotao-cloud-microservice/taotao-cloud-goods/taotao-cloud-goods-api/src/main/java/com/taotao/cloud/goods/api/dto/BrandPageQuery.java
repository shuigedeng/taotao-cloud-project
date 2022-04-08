package com.taotao.cloud.goods.api.dto;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品品牌dto
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品品牌dto")
public class BrandPageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = 8906820486037326039L;

	@Schema(description = "品牌名称")
	private String name;
}
