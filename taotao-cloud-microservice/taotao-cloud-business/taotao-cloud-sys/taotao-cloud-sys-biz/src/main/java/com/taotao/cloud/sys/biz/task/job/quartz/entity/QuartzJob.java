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
import com.taotao.boot.job.quartz.enums.QuartzJobCode;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * QuartzJob
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = QuartzJob.TABLE_NAME)
@TableName(QuartzJob.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = QuartzJob.TABLE_NAME, comment = "Quartz任务表")
public class QuartzJob extends BaseSuperEntity<QuartzJob, Long> {

    public static final String TABLE_NAME = "tt_quartz_job";

    public static final String JOB_KEY = "JOB_KEY";

    /**
     * 任务名称
     */
    @Column(name = "job_name", columnDefinition = "varchar(64) not null comment '任务名称'")
    private String jobName;

    /**
     * 组名称
     */
    @Column(name = "group_name", columnDefinition = "varchar(64) not null comment '组名称'")
    private String groupName;

    /**
     * Spring Bean名称
     */
    @Column(name = "bean_name", columnDefinition = "varchar(64) not null comment ' Spring Bean名称'")
    private String beanName;

    /**
     * cron 表达式
     */
    @Column(name = "cron_expression", columnDefinition = "varchar(64) not null comment 'cron 表达式'")
    private String cronExpression;

    /**
     * @see QuartzJobCode
     */
    @Column(name = "state", columnDefinition = "int default 1 comment '定时任务状态'")
    private Integer state;

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
     * 备注
     */
    @Column(name = "remark", columnDefinition = "varchar(125) comment '备注'")
    private String remark;

    /**
     * 是否并发 0：禁止 1：允许
     */
    @Column(name = "concurrent", columnDefinition = "int default 1 comment '是否并发  0：禁止  1：允许'")
    private Integer concurrent;
}
