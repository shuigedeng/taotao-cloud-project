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
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Bpm 流程实例的拓展表 主要解决 Activiti ProcessInstance 和 HistoricProcessInstance 不支持拓展字段，所以新建拓展表
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_process_instance_ext", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmProcessInstanceExtDO extends BaseDO {

    /** 编号，自增 */
    @TableId
    private Long id;
    /**
     * 发起流程的用户编号
     *
     * <p>冗余 HistoricProcessInstance 的 startUserId 属性
     */
    private Long startUserId;
    /**
     * 流程实例的名字
     *
     * <p>冗余 ProcessInstance 的 name 属性，用于筛选
     */
    private String name;
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
    /**
     * 流程分类
     *
     * <p>冗余 ProcessDefinition 的 category 属性 数据字典 bpm_model_category
     */
    private String category;
    /**
     * 流程实例的状态
     *
     * <p>枚举 {@link BpmProcessInstanceStatusEnum}
     */
    private Integer status;
    /**
     * 流程实例的结果
     *
     * <p>枚举 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;
    /**
     * 结束时间
     *
     * <p>冗余 HistoricProcessInstance 的 endTime 属性
     */
    private LocalDateTime endTime;

    /** 提交的表单值 */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> formVariables;
}
