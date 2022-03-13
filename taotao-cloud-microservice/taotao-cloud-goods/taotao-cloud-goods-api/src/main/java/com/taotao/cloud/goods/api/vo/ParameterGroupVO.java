package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 参数组vo
 */
@Data
public class ParameterGroupVO implements Serializable {

	private static final long serialVersionUID = 724427321881170297L;
	@Schema(description = "参数组关联的参数集合")
	private List<Parameters> params;
	@Schema(description = "参数组名称")
	private String groupName;
	@Schema(description = "参数组id")
	private String groupId;


}
