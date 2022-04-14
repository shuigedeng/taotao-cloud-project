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
 * 规格查询参数
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "规格查询参数")
public class SpecificationPageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = 8906820486037326039L;

	@Schema(description = "名称")
	private String specName;
}
