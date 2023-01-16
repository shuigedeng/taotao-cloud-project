package com.taotao.cloud.workflow.biz.engine.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.util.Date;
import lombok.Data;

/**
 * 流程经办记录
 *
 */
@Data
@TableName("flow_taskoperatorrecord")
public class FlowTaskOperatorRecordEntity extends SuperEntity<FlowTaskOperatorRecordEntity, String> {
    /**
     * 节点流转主键
     */
    @TableId("id")
    private String id;

    /**
     * 节点编码
     */
    @TableField("node_code")
    private String nodeCode;

    /**
     * 节点名称
     */
    @TableField("node_name")
    private String nodeName;

    /**
     * 经办状态 0-拒绝、1-同意、2-提交、3-撤回、4-终止、5-指派、6-加签、7-转办
     */
    @TableField("handle_status")
    private Integer handleStatus;

    /**
     * 经办人员
     */
    @TableField("handle_id")
    private String handleId;

    /**
     * 经办时间
     */
    @TableField("handle_time")
    private Date handleTime;

    /**
     * 经办理由
     */
    @TableField("handle_opinion")
    private String handleOpinion;

    /**
     * 流转操作人
     */
    @TableField("operator_id")
    private String operatorId;

    /**
     * 经办主键
     */
    @TableField(value="task_operator_id",fill = FieldFill.UPDATE)
    private String taskOperatorId;

    /**
     * 节点主键
     */
    @TableField(value="task_node_id",fill = FieldFill.UPDATE)
    private String taskNodeId;

    /**
     * 任务主键
     */
    @TableField("task_id")
    private String taskId;

    /**
     * 签名图片
     */
    @TableField("sign_img")
    private String signImg;

    /**
     * 0.进行数据 -1.作废数据 1.加签数据 3.已办不显示数据
     */
    @TableField("status")
    private Integer status;
}
