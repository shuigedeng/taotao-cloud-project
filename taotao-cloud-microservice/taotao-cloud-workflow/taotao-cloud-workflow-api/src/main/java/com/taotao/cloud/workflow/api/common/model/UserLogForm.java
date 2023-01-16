package com.taotao.cloud.workflow.api.common.model;

import com.taotao.cloud.workflow.api.common.base.Pagination;
import lombok.Data;

/**
 *
 */
@Data
public class UserLogForm extends Pagination {
    private String startTime;
    private String endTime;
    private int category;
}
