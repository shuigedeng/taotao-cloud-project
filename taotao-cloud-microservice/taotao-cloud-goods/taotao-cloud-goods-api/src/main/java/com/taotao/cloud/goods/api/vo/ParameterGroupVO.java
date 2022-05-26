package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 参数组vo
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@RecordBuilder
public record ParameterGroupVO(

	@Schema(description = "参数组关联的参数集合")
	List<ParametersVO> params,

	@Schema(description = "参数组名称")
	String groupName,

	@Schema(description = "参数组id")
	Long groupId
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 724427321881170297L;


}
