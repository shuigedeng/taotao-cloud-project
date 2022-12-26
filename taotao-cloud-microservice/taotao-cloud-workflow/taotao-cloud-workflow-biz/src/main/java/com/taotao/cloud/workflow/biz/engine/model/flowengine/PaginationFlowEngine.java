package com.taotao.cloud.workflow.biz.engine.model.flowengine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.common.model.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaginationFlowEngine extends PageParam {

	private Integer formType;
	private Integer enabledMark;

	@Tag(hidden = true)
	@JsonIgnore
	private Integer type;
}
