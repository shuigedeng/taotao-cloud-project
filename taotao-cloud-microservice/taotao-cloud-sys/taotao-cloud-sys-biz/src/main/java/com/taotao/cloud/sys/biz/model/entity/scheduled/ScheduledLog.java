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
package com.taotao.cloud.sys.biz.model.entity.scheduled;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Scheduled日志表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ScheduledLog.TABLE_NAME)
@TableName(ScheduledLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ScheduledLog.TABLE_NAME, comment = "Scheduled日志表")
public class ScheduledLog extends BaseSuperEntity<ScheduledLog, Long> {

	public static final String TABLE_NAME = "tt_scheduled_log";

	/**
	 * 调度器名称
	 */
	@Column(name = "scheduled_name", columnDefinition = "varchar(64) not null comment '调度器名称'")
	private String scheduledName;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time", columnDefinition = "TIMESTAMP comment '开始时间'")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "end_time", columnDefinition = "TIMESTAMP comment '结束时间'")
	private LocalDateTime endTime;

	/**
	 * 异常信息
	 */
	@Column(name = "exception", columnDefinition = "varchar(4096) comment '异常信息'")
	private String exception;

	/**
	 * 执行时间
	 */
	@Column(name = "execution_time", columnDefinition = "bigint default 0  comment '执行时间'")
	private Long executionTime;

	/**
	 * 是否成功
	 */
	@Column(name = "is_success", columnDefinition = "boolean default false comment '是否成功'")
	private Boolean isSuccess;

	/**
	 * 调度器名称
	 */
	@Column(name = "scheduled_Job", columnDefinition = "varchar(4096) not null comment 'scheduledJob JSON对象'")
	private String scheduledJob;
	@Builder
	public ScheduledLog(Long id, LocalDateTime createTime, Long createBy, LocalDateTime updateTime,
		Long updateBy, Integer version, Boolean delFlag, String scheduledName,
		LocalDateTime startTime, LocalDateTime endTime, String exception, Long executionTime,
		Boolean isSuccess, String scheduledJob) {
		super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
		this.scheduledName = scheduledName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.exception = exception;
		this.executionTime = executionTime;
		this.isSuccess = isSuccess;
		this.scheduledJob = scheduledJob;
	}

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		ScheduledLog that = (ScheduledLog) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
