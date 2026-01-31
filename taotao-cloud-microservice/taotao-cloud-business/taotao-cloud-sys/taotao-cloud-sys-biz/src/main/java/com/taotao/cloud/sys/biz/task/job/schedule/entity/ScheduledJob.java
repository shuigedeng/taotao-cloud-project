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

package com.taotao.cloud.sys.biz.task.job.schedule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ScheduledJob
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
@Table(name = ScheduledJob.TABLE_NAME)
@TableName(ScheduledJob.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = ScheduledJob.TABLE_NAME, comment = "Scheduled任务表")
public class ScheduledJob extends BaseSuperEntity<ScheduledJob, String> {

    public static final String TABLE_NAME = "tt_scheduled_job";

    private static final long serialVersionUID = 5126530068827085130L;

    // 任务名
    @Column(name = "name", columnDefinition = "varchar(64) not null comment '任务名'")
    private String name;

    /*
    目标字符串
    格式bean.method(params)
    String字符串类型，包含'、boolean布尔类型，等于true或者false
    long长整形，包含L、double浮点类型，包含D、其他类型归类为整形
    aa.aa('String',100L,20.20D)
    */
    @Column(name = "invoke_target", columnDefinition = "varchar(125) not null comment '目标字符串'")
    private String invokeTarget;

    // 周期(month、week、day、hour、minute、secods)
    @Column(name = "cycle", columnDefinition = "varchar(125) not null comment '周期(month、week、day、hour、minute、secods)'")
    private String cycle;

    // cron表达式
    @Column(name = "cron_expression", columnDefinition = "varchar(125) not null comment 'cron表达式'")
    private String cronExpression;

    // 执行策略(1手动，2-自动）
    @Column(name = "policy", columnDefinition = "int not null default 2 comment '执行策略(1手动，2-自动）'")
    private Integer policy;

    // 状态（0正常 1暂停）
    @Column(name = "status", columnDefinition = "int not null default 0 comment '状态（0正常 1暂停）'")
    private Integer status;

    // 执行情况(1-执行中,2-已暂停)
    @Column(name = "situation", columnDefinition = "int not null default 0 comment '执行情况(1-执行中,2-已暂停)'")
    private Integer situation;

    // 上次执行时间
    @Column(name = "last_run_time", columnDefinition = "DATETIME null comment '上次执行时间'")
    private Date lastRunTime;

    // 下次执行时间
    @Column(name = "next_run_time", columnDefinition = "DATETIME null comment '下次执行时间'")
    private Date nextRunTime;

    // 备注
    @Column(name = "remark", columnDefinition = "varchar(125) null comment '备注'")
    private String remark;
}
