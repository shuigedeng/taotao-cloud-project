package com.taotao.cloud.workflow.biz.engine.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 流程经办
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("flow_taskoperator")
public class FlowTaskOperatorEntity {
    /**
     * 节点经办主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 经办对象
     */
    @TableField("F_HANDLETYPE")
    private String handleType;

    /**
     * 节点类型
     */
    @TableField("F_Type")
    private String type;

    /**
     * 经办主键
     */
    @TableField("F_HANDLEID")
    private String handleId;

    /**
     * 处理状态 0-拒绝、1-同意
     */
    @TableField(value = "F_HANDLESTATUS",fill = FieldFill.UPDATE)
    private Integer handleStatus;

    /**
     * 处理时间
     */
    @TableField(value = "F_HANDLETIME",fill = FieldFill.UPDATE)
    private Date handleTime;

    /**
     * 节点编码
     */
    @TableField("F_NODECODE")
    private String nodeCode;

    /**
     * 节点名称
     */
    @TableField("F_NODENAME")
    private String nodeName;

    /**
     * 是否完成
     */
    @TableField("F_COMPLETION")
    private Integer completion;

    /**
     * 描述
     */
    @TableField("F_DESCRIPTION")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "F_CREATORTIME",fill = FieldFill.INSERT)
    private Date creatorTime;

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
     * 状态 0.新流程 -1.无用数据
     */
    @TableField("F_STATE")
    private Integer state;

    /**
     * 父节点id
     */
    @TableField("F_PARENTID")
    private String parentId;

    /**
     * 草稿数据
     */
    @TableField(value = "F_DRAFTDATA",fill = FieldFill.UPDATE)
    private String draftData;
}
