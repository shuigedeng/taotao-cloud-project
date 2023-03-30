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

/** 流程节点 */
@Data
@TableName("flow_tasknode")
public class FlowTaskNodeEntity extends SuperEntity<FlowTaskNodeEntity, String> {
    /** 节点实例主键 */
    @TableId("id")
    private String id;

    /** 节点编码 */
    @TableField("node_code")
    private String nodeCode;

    /** 节点名称 */
    @TableField("node_name")
    private String nodeName;

    /** 节点类型 */
    @TableField("node_type")
    private String nodeType;

    /** 节点属性Json */
    @TableField("node_property_json")
    private String nodePropertyJson;

    /** 上一节点 1.上一步骤 0.返回开始 */
    @TableField("node_up")
    private String nodeUp;

    /** 下一节点 */
    @TableField("node_next")
    private String nodeNext;

    /** 是否完成 */
    @TableField("completion")
    private Integer completion;

    /** 描述 */
    @TableField("description")
    private String description;

    /** 排序码 */
    @TableField("sort_code")
    private Long sortCode;

    /** 创建时间 */
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 任务主键 */
    @TableField("task_id")
    private String taskId;

    /** 状态 0.新流程 -1.无用节点 */
    @TableField("state")
    private Integer state;

    /** 候选人 */
    @TableField("candidates")
    private String candidates;
}
