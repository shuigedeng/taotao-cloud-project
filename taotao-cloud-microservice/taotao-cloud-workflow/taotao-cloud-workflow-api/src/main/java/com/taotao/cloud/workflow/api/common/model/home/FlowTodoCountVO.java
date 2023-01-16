package com.taotao.cloud.workflow.api.common.model.home;

import lombok.Data;

/**
 *
 */
@Data
public class FlowTodoCountVO {
    private Integer toBeReviewed;
    private Integer entrust;
    private Integer flowDone;
}
