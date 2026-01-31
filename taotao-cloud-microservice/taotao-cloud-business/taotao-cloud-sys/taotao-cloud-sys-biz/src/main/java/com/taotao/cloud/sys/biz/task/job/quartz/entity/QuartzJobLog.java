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

package com.taotao.cloud.sys.biz.task.job.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * QuartzJobLog
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = QuartzJobLog.TABLE_NAME)
@TableName(QuartzJobLog.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = QuartzJobLog.TABLE_NAME, comment = "Quartz任务日志表")
public class QuartzJobLog extends BaseSuperEntity<QuartzJobLog, Long> {

    public static final String TABLE_NAME = "tt_quartz_job_log";

    /**
     * 任务id
     */
    @Column(name = "job_id", columnDefinition = "bigint not null comment '任务id'")
    private Long jobId;

    /**
     * 任务名称
     */
    @Column(name = "job_name", columnDefinition = "varchar(64) not null comment '任务名称'")
    private String jobName;

    /**
     * 任务组名
     */
    @Column(name = "job_group", columnDefinition = "varchar(64) not null comment '任务组名'")
    private String jobGroup;

    @Column(name = "bean_name", columnDefinition = "varchar(64) not null comment 'springbean名称'")
    private String beanName;

    @Column(name = "class_name", columnDefinition = "varchar(64) not null comment '类名称'")
    private String className;

    /**
     * cron表达式
     */
    @Column(name = "cron_expression", columnDefinition = "varchar(64) not null comment 'cron表达式'")
    private String cronExpression;

    /**
     * 异常详细
     */
    @Column(name = "exception_detail", columnDefinition = "text comment '异常详细'")
    private String exceptionDetail;

    /**
     * 状态
     */
    @Column(name = "is_success", columnDefinition = "boolean comment '状态'")
    private Boolean isSuccess;

    /**
     * 方法名称
     */
    @Column(name = "method_name", columnDefinition = "varchar(64) not null comment '方法名称'")
    private String methodName;

    /**
     * 参数
     */
    @Column(name = "params", columnDefinition = "varchar(125) comment '参数'")
    private String params;

    /**
     * 耗时（毫秒）
     */
    @Column(name = "time", columnDefinition = "bigint comment '耗时（毫秒）'")
    private Long time;

    /**
     * 结束时间
     */
    @Column(name = "end_time", columnDefinition = "datetime comment '结束时间'")
    private LocalDateTime endTime;

    /**
     * 开始时间
     */
    @Column(name = "start_time", columnDefinition = "datetime comment '开始时间'")
    private LocalDateTime startTime;

    /**
     * 执行线程
     */
    @Column(name = "execution_thread", columnDefinition = "varchar(1024) comment '执行线程'")
    private String executionThread;
}
