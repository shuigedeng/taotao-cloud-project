package com.taotao.cloud.workflow.api.common.model.engine.flowengine;

import com.taotao.cloud.common.model.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlowPagination extends PageQuery {

	private String category;
}
