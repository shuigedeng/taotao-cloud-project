package com.taotao.cloud.workflow.api.common.model.home;

import lombok.Data;

/**
 *
 */
@Data
public class MyFlowTodoVO {
    private String id;
    private Integer enabledMark;
    private Long startTime;
    private Long endTime;
    private String content;
}
