package com.taotao.cloud.workflow.biz.engine.model.flowengine;

import com.taotao.cloud.common.model.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlowPagination extends PageParam {

	private String category;
}
