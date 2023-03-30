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

/** 流程传阅 */
@Data
@TableName("flow_taskcirculate")
public class FlowTaskCirculateEntity extends SuperEntity<FlowTaskCirculateEntity, String> {
    /** 传阅主键 */
    @TableId("id")
    private String id;

    /** 对象类型 */
    @TableField("object_type")
    private String objectType;

    /** 对象主键 */
    @TableField("object_id")
    private String objectId;

    /** 节点编码 */
    @TableField("node_code")
    private String nodeCode;

    /** 节点名称 */
    @TableField("node_name")
    private String nodeName;

    /** 节点主键 */
    @TableField("task_node_id")
    private String taskNodeId;

    /** 任务主键 */
    @TableField("task_id")
    private String taskId;

    /** 创建时间 */
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;
}
