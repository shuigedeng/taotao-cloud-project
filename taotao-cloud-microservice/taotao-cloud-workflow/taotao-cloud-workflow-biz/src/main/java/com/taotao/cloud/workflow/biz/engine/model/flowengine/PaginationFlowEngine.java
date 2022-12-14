package com.taotao.cloud.workflow.biz.engine.model.flowengine;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 *
 */
@Data
public class PaginationFlowEngine extends Pagination {
    private Integer formType;
    private Integer enabledMark;
    @TagModelProperty(hidden = true)
    @JsonIgnore
    private Integer type;
}
