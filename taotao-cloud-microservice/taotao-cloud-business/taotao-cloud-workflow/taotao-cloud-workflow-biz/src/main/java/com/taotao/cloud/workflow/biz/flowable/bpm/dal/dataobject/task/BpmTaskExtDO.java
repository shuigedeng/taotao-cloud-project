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

package com.taotao.cloud.workflow.biz.flowable.bpm.dal.dataobject.task;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Bpm 流程任务的拓展表 主要解决 Flowable Task 和 HistoricTaskInstance 不支持拓展字段，所以新建拓展表
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_task_ext", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskExtDO extends BaseDO {

    /** 编号，自增 */
    @TableId
    private Long id;

    /**
     * 任务的审批人
     *
     * <p>冗余 Task 的 assignee 属性
     */
    private Long assigneeUserId;
    /**
     * 任务的名字
     *
     * <p>冗余 Task 的 name 属性，为了筛选
     */
    private String name;
    /**
     * 任务的编号
     *
     * <p>关联 Task 的 id 属性
     */
    private String taskId;
    //    /**
    //     * 任务的标识
    //     *
    //     * 关联 {@link Task#getTaskDefinitionKey()}
    //     */
    //    private String definitionKey;
    /**
     * 任务的结果
     *
     * <p>枚举 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;
    /** 审批建议 */
    private String reason;
    /**
     * 任务的结束时间
     *
     * <p>冗余 HistoricTaskInstance 的 endTime 属性
     */
    private LocalDateTime endTime;

    /**
     * 流程实例的编号
     *
     * <p>关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 流程定义的编号
     *
     * <p>关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;
}
