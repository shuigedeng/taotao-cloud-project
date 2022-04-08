package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数组vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParameterGroupVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 724427321881170297L;

	@Schema(description = "参数组关联的参数集合")
	private List<ParametersVO> params;

	@Schema(description = "参数组名称")
	private String groupName;

	@Schema(description = "参数组id")
	private Long groupId;


}
