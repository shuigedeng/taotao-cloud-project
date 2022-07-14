package com.taotao.cloud.workflow.biz.engine.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 */
@Data
public class FlowHandleModel extends Pagination {
    /**意见**/
    private String handleOpinion;
    /**指定人**/
    private String freeApproverUserId;
    /**表单数据**/
    private Map<String,Object> formData;
    /**编码**/
    private String enCode;
    /**自定义抄送人**/
    private String copyIds;
    /**签名**/
    private String signImg;
    /**指派节点**/
    private String nodeCode;
    /**
     * 候选人
     */
    private Map<String, List<String>> candidateList;
    /**
     * 批量审批id
     */
    private List<String> ids;
    /**
     * 批量审批类型 0.通过 1.拒绝 2.转办
     */
    private Integer batchType;

}
