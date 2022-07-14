package com.taotao.cloud.workflow.biz.engine.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 流程任务
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("flow_task")
public class FlowTaskEntity {
    /**
     * 任务主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 实例进程
     */
    @TableField("F_PROCESSID")
    private String processId;

    /**
     * 任务编码
     */
    @TableField("F_ENCODE")
    private String enCode;

    /**
     * 任务标题
     */
    @TableField("F_FULLNAME")
    private String fullName;

    /**
     * 紧急程度
     */
    @TableField("F_FLOWURGENT")
    private Integer flowUrgent;

    /**
     * 流程主键
     */
    @TableField("F_FLOWID")
    private String flowId;

    /**
     * 流程编码
     */
    @TableField("F_FLOWCODE")
    private String flowCode;

    /**
     * 流程名称
     */
    @TableField("F_FLOWNAME")
    private String flowName;

    /**
     * 流程类型
     */
    @TableField("F_FLOWTYPE")
    private Integer flowType;

    /**
     * 流程分类
     */
    @TableField("F_FLOWCATEGORY")
    private String flowCategory;

    /**
     * 流程表单
     */
    @TableField("F_FLOWFORM")
    private String flowForm;

    /**
     * 表单内容
     */
    @TableField("F_FLOWFORMCONTENTJSON")
    private String flowFormContentJson;

    /**
     * 流程模板
     */
    @TableField("F_FLOWTEMPLATEJSON")
    private String flowTemplateJson;

    /**
     * 流程版本
     */
    @TableField("F_FLOWVERSION")
    private String flowVersion;

    /**
     * 开始时间
     */
    @TableField(value = "F_STARTTIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "F_ENDTIME")
    private Date endTime;

    /**
     * 当前步骤
     */
    @TableField("F_THISSTEP")
    private String thisStep;

    /**
     * 当前步骤Id
     */
    @TableField(value = "F_THISSTEPID")
    private String thisStepId;

    /**
     * 重要等级
     */
    @TableField("F_GRADE")
    private String grade;

    /**
     * 任务状态 0-草稿、1-处理、2-通过、3-驳回、4-撤销、5-终止
     */
    @TableField("F_STATUS")
    private Integer status;

    /**
     * 完成情况
     */
    @TableField("F_COMPLETION")
    private Integer completion;

    /**
     * 描述
     */
    @TableField("F_DESCRIPTION")
    private String description;

    /**
     * 父节点id
     */
    @TableField("F_PARENTID")
    private String parentId;

    /**
     * 是否批量（0：否，1：是）
     */
    @TableField("F_ISBATCH")
    private Integer isBatch;

    /**
     * 排序码
     */
    @TableField("F_SORTCODE")
    private Long sortCode;

    /**
     * 有效标志
     */
    @TableField("F_ENABLEDMARK")
    private Integer enabledMark;

    /**
     * 同步异步（0：同步，1：异步）
     */
    @TableField(value = "F_ISASYNC")
    private Integer isAsync;

    /**
     * 创建时间
     */
    @TableField(value = "F_CREATORTIME", fill = FieldFill.INSERT)
    private Date creatorTime;

    /**
     * 创建用户
     */
    @TableField(value = "F_CREATORUSERID", fill = FieldFill.INSERT)
    private String creatorUserId;

    /**
     * 修改时间
     */
    @TableField(value = "F_LASTMODIFYTIME", fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /**
     * 修改用户
     */
    @TableField(value = "F_LASTMODIFYUSERID", fill = FieldFill.UPDATE)
    private String lastModifyUserId;

    /**
     * 删除标志
     */
    @TableField("F_DELETEMARK")
    private Integer deleteMark;

    /**
     * 删除时间
     */
    @TableField("F_DELETETIME")
    private Date deleteTime;

    /**
     * 删除用户
     */
    @TableField("F_DELETEUSERID")
    private String deleteUserId;
}
