package com.taotao.cloud.workflow.biz.common.model.engine.flowtask;

import java.util.List;
import java.util.Map;
import lombok.Data;


@Data
public class FlowTaskForm {
    /**引擎id**/
    private String flowId;
    /**界面数据**/
    private String data;
    /**0.提交 1.保存**/
    private String status;
    /**指定用户**/
    private String freeApproverUserId;
    /**
     * 候选人
     */
    private Map<String, List<String>> candidateList;
}
