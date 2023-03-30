/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.engine.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.util.Date;
import lombok.Data;

/** 流程任务 */
@Data
@TableName("flow_task")
public class FlowTaskEntity extends SuperEntity<FlowTaskEntity, String> {

    /** 任务主键 */
    @TableId("id")
    private String id;

    /** 实例进程 */
    @TableField("process_id")
    private String processId;

    /** 任务编码 */
    @TableField("en_code")
    private String enCode;

    /** 任务标题 */
    @TableField("full_name")
    private String fullName;

    /** 紧急程度 */
    @TableField("flow_urgent")
    private Integer flowUrgent;

    /** 流程主键 */
    @TableField("flow_id")
    private String flowId;

    /** 流程编码 */
    @TableField("flow_code")
    private String flowCode;

    /** 流程名称 */
    @TableField("flow_name")
    private String flowName;

    /** 流程类型 */
    @TableField("flow_type")
    private Integer flowType;

    /** 流程分类 */
    @TableField("flow_category")
    private String flowCategory;

    /** 流程表单 */
    @TableField("flow_form")
    private String flowForm;

    /** 表单内容 */
    @TableField("flow_form_content_json")
    private String flowFormContentJson;

    /** 流程模板 */
    @TableField("flow_template_json")
    private String flowTemplateJson;

    /** 流程版本 */
    @TableField("flow_version")
    private String flowVersion;

    /** 开始时间 */
    @TableField(value = "start_time")
    private Date startTime;

    /** 结束时间 */
    @TableField(value = "end_time")
    private Date endTime;

    /** 当前步骤 */
    @TableField("this_step")
    private String thisStep;

    /** 当前步骤Id */
    @TableField(value = "this_stepid")
    private String thisStepId;

    /** 重要等级 */
    @TableField("grade")
    private String grade;

    /** 任务状态 0-草稿、1-处理、2-通过、3-驳回、4-撤销、5-终止 */
    @TableField("status")
    private Integer status;

    /** 完成情况 */
    @TableField("completion")
    private Integer completion;

    /** 描述 */
    @TableField("description")
    private String description;

    /** 父节点id */
    @TableField("parent_id")
    private String parentId;

    /** 是否批量（0：否，1：是） */
    @TableField("is_batch")
    private Integer isBatch;

    /** 排序码 */
    @TableField("sort_code")
    private Long sortCode;

    /** 有效标志 */
    @TableField("enabled_mark")
    private Integer enabledMark;

    /** 同步异步（0：同步，1：异步） */
    @TableField(value = "is_async")
    private Integer isAsync;

    /** 创建时间 */
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 创建用户 */
    @TableField(value = "creator_user_id", fill = FieldFill.INSERT)
    private Long creatorUserId;

    /** 修改时间 */
    @TableField(value = "lastmodify_time", fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /** 修改用户 */
    @TableField(value = "lastmodify_user_id", fill = FieldFill.UPDATE)
    private String lastModifyUserId;

    /** 删除标志 */
    @TableField("delete_mark")
    private Integer deleteMark;

    /** 删除时间 */
    @TableField("delete_time")
    private Date deleteTime;

    /** 删除用户 */
    @TableField("delete_user_id")
    private String deleteUserId;
}
