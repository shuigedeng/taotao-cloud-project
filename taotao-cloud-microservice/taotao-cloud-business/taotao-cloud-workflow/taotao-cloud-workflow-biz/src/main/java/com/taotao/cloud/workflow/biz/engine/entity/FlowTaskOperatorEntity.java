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

/** 流程经办 */
@Data
@TableName("flow_taskoperator")
public class FlowTaskOperatorEntity extends SuperEntity<FlowTaskOperatorEntity, String> {
    /** 节点经办主键 */
    @TableId("id")
    private String id;

    /** 经办对象 */
    @TableField("handle_type")
    private String handleType;

    /** 节点类型 */
    @TableField("type")
    private String type;

    /** 经办主键 */
    @TableField("handle_id")
    private String handleId;

    /** 处理状态 0-拒绝、1-同意 */
    @TableField(value = "handle_status", fill = FieldFill.UPDATE)
    private Integer handleStatus;

    /** 处理时间 */
    @TableField(value = "handle_time", fill = FieldFill.UPDATE)
    private Date handleTime;

    /** 节点编码 */
    @TableField("node_code")
    private String nodeCode;

    /** 节点名称 */
    @TableField("node_name")
    private String nodeName;

    /** 是否完成 */
    @TableField("completion")
    private Integer completion;

    /** 描述 */
    @TableField("description")
    private String description;

    /** 创建时间 */
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 节点主键 */
    @TableField("task_node_id")
    private String taskNodeId;

    /** 任务主键 */
    @TableField("task_id")
    private String taskId;

    /** 状态 0.新流程 -1.无用数据 */
    @TableField("state")
    private Integer state;

    /** 父节点id */
    @TableField("parent_id")
    private String parentId;

    /** 草稿数据 */
    @TableField(value = "draft_data", fill = FieldFill.UPDATE)
    private String draftData;
}
