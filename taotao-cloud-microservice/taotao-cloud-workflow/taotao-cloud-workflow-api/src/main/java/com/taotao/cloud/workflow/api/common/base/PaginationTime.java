package com.taotao.cloud.workflow.api.common.base;

import lombok.Data;

/**
 */
@Data
public class PaginationTime extends Pagination{
    private String startTime;
    private String endTime;
//    private String type;
}
