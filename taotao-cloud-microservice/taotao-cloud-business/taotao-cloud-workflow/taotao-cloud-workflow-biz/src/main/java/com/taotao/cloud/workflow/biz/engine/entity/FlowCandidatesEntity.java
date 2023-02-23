package com.taotao.cloud.workflow.biz.engine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import lombok.Data;


/**
 * 流程候选人
 */
@Data
@TableName("flow_candidates")
public class FlowCandidatesEntity extends SuperEntity<FlowCandidatesEntity, String> {
    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 节点主键
     */
    @TableField("task_node_id")
    private String taskNodeId;

    /**
     * 任务主键
     */
    @TableField("task_id")
    private String taskId;

    /**
     * 代办主键
     */
    @TableField("task_operator_id")
    private String operatorId;

    /**
     * 审批人主键
     */
    @TableField("handle_id")
    private String handleId;

    /**
     * 审批人账号
     */
    @TableField("account")
    private String account;

    /**
     * 候选人
     */
    @TableField("candidates")
    private String candidates;

}
