package com.taotao.cloud.workflow.biz.engine.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 流程委托
 */
@Data
@TableName("flow_delegate")
public class FlowDelegateEntity {
    /**
     * 委托主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 被委托人
     */
    @TableField("F_TOUSERID")
    @JSONField(name = "toUserId")
    private String fTouserid;

    /**
     * 被委托人
     */
    @TableField("F_TOUSERNAME")
    private String toUserName;

    /**
     * 委托流程
     */
    @TableField("F_FLOWID")
    private String flowId;

    /**
     * 委托流程
     */
    @TableField("F_FLOWNAME")
    private String flowName;

    /**
     * 流程分类
     */
    @TableField("F_FLOWCATEGORY")
    private String flowCategory;

    /**
     * 开始时间
     */
    @TableField("F_STARTTIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("F_ENDTIME")
    private Date endTime;

    /**
     * 描述
     */
    @TableField("F_DESCRIPTION")
    private String description;

    /**
     * 排序码
     */
    @TableField("F_SORTCODE")
    private Long fSortCode;

    /**
     * 有效标志
     */
    @TableField("F_ENABLEDMARK")
    private Integer enabledMark;

    /**
     * 创建时间
     */
    @TableField(value = "F_CREATORTIME",fill = FieldFill.INSERT)
    private Date creatorTime;

    /**
     * 创建用户
     */
    @TableField(value = "F_CREATORUSERID",fill = FieldFill.INSERT)
    private String creatorUserId;

    /**
     * 修改时间
     */
    @TableField(value = "F_LASTMODIFYTIME",fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /**
     * 修改用户
     */
    @TableField(value = "F_LASTMODIFYUSERID",fill = FieldFill.UPDATE)
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
