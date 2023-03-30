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

/** 流程可见 */
@Data
@TableName("flow_enginevisible")
public class FlowEngineVisibleEntity extends SuperEntity<FlowEngineVisibleEntity, String> {
    /** 可见主键 */
    @TableId("id")
    private String id;

    /** 流程主键 */
    @TableField("flow_id")
    private String flowId;

    /** 经办类型 */
    @TableField("operator_type")
    private String operatorType;

    /** 经办主键 */
    @TableField("operator_id")
    private String operatorId;

    /** 排序码 */
    @TableField("sort_code")
    private Long sortCode;

    /** 创建时间 */
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 创建用户 */
    @TableField(value = "creator_user_id", fill = FieldFill.INSERT)
    private String creatorUserId;
}
