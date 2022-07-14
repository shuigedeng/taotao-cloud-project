package com.taotao.cloud.workflow.biz.engine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 流程候选人
 */
@Data
@TableName("flow_candidates")
public class FlowCandidatesEntity {
    /**
     * 主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 节点主键
     */
    @TableField("F_TASKNODEID")
    private String taskNodeId;

    /**
     * 任务主键
     */
    @TableField("F_TASKID")
    private String taskId;

    /**
     * 代办主键
     */
    @TableField("F_TASKOPERATORID")
    private String operatorId;

    /**
     * 审批人主键
     */
    @TableField("F_HANDLEID")
    private String handleId;

    /**
     * 审批人账号
     */
    @TableField("F_ACCOUNT")
    private String account;

    /**
     * 候选人
     */
    @TableField("F_CANDIDATES")
    private String candidates;

}
