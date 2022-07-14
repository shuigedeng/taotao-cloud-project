package com.taotao.cloud.workflow.biz.engine.model.flowtask;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:17
 */
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
