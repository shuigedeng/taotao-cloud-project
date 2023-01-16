package com.taotao.cloud.workflow.api.common.model.engine.flowengine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.common.model.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaginationFlowEngine extends PageQuery {

	private Integer formType;
	private Integer enabledMark;

	@Tag(hidden = true)
	@JsonIgnore
	private Integer type;
}
