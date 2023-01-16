package com.taotao.cloud.workflow.api.common.model.engine.flowcomment;
import com.taotao.cloud.workflow.api.common.base.Pagination;
import lombok.Data;

/**
 */
@Data
public class FlowCommentPagination extends Pagination {

    private String taskId;

}
