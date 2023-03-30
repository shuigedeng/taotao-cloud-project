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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import lombok.Data;

/** 流程候选人 */
@Data
@TableName("flow_candidates")
public class FlowCandidatesEntity extends SuperEntity<FlowCandidatesEntity, String> {
    /** 主键 */
    @TableId("id")
    private String id;

    /** 节点主键 */
    @TableField("task_node_id")
    private String taskNodeId;

    /** 任务主键 */
    @TableField("task_id")
    private String taskId;

    /** 代办主键 */
    @TableField("task_operator_id")
    private String operatorId;

    /** 审批人主键 */
    @TableField("handle_id")
    private String handleId;

    /** 审批人账号 */
    @TableField("account")
    private String account;

    /** 候选人 */
    @TableField("candidates")
    private String candidates;
}
