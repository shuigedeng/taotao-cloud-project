package com.taotao.cloud.workflow.biz.engine.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 流程引擎
 */
@Data
@TableName("flow_engine")
public class FlowEngineEntity {
    /**
     * 流程主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 流程编码
     */
    @TableField("F_ENCODE")
    private String enCode;

    /**
     * 流程名称
     */
    @TableField("F_FULLNAME")
    private String fullName;

    /**
     * 流程类型(0.发起流程 1.功能流程)
     */
    @TableField("F_TYPE")
    private Integer type;

    /**
     * 流程分类
     */
    @TableField("F_CATEGORY")
    private String category;

    /**
     * 可见类型 0-全部可见、1-指定经办
     */
    @TableField("F_VISIBLETYPE")
    private Integer visibleType;

    /**
     * 图标
     */
    @TableField("F_ICON")
    private String icon;

    /**
     * 图标背景色
     */
    @TableField("F_ICONBACKGROUND")
    private String iconBackground;

    /**
     * 流程版本
     */
    @TableField("F_VERSION")
    private String version;

    /**
     * 表单字段
     */
    @TableField("F_FormTemplateJson")
    private String formData;

    /**
     * 表单分类(1.系统表单 2.自定义表单)
     */
    @TableField("F_FORMTYPE")
    private Integer formType;

    /**
     * 流程引擎
     */
    @TableField("F_FLOWTEMPLATEJSON")
    private String flowTemplateJson;

    /**
     * 描述
     */
    @TableField("F_DESCRIPTION")
    private String description;

    /**
     * 列表
     */
    @TableField("F_TABLES")
    @JSONField(name = "tables")
    private String flowTables;

    /**
     * 数据连接
     */
    @TableField("F_DBLINKID")
    private String dbLinkId;

    /**
     * app表单路径
     */
    @TableField("F_APPFORMURL")
    private String appFormUrl;

    /**
     * pc表单路径
     */
    @TableField("F_FORMURL")
    private String formUrl;

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
     * 创建时间
     */
    @TableField(value = "F_CREATORTIME",fill = FieldFill.INSERT)
    private Date creatorTime;

    /**
     * 创建用户
     */
    @TableField(value = "F_CREATORUSERID",fill = FieldFill.INSERT)
    private String creatorUser;

    /**
     * 修改时间
     */
    @TableField(value = "F_LASTMODIFYTIME",fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /**
     * 修改用户
     */
    @TableField(value = "F_LASTMODIFYUSERID",fill = FieldFill.UPDATE)
    private String lastModifyUser;

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
