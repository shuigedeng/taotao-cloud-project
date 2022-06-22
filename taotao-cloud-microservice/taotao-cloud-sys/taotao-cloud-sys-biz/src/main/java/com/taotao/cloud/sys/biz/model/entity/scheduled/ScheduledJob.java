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
import com.taotao.cloud.web.schedule.enums.ScheduledType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Scheduled任务表
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
@Builder
@Entity
@Table(name = ScheduledJob.TABLE_NAME)
@TableName(ScheduledJob.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ScheduledJob.TABLE_NAME, comment = "Scheduled任务表")
public class ScheduledJob extends BaseSuperEntity<ScheduledJob, Long> {

	public static final String TABLE_NAME = "tt_scheduled_job";

	/**
	 * cron表达式
	 */
	@Column(name = "cron", columnDefinition = "varchar(64) not null comment 'cron表达式'")
	private String cron;

	/**
	 * 时区，cron表达式会基于该时区解析
	 */
	@Column(name = "zone", columnDefinition = "varchar(64) not null comment '时区，cron表达式会基于该时区解析'")
	private String zone;

	/**
	 * 上一次执行完毕时间点之后多长时间再执行
	 */
	@Column(name = "fixed_delay", columnDefinition = "bigint not null default 0 comment '上一次执行完毕时间点之后多长时间再执行'")
	private Long fixedDelay;

	/**
	 * 与 fixedDelay 意思相同，只是使用字符串的形式
	 */
	@Column(name = "fixed_delay_string", columnDefinition = "varchar(64) not null comment '与 fixedDelay 意思相同，只是使用字符串的形式'")
	private String fixedDelayString;

	/**
	 * 上一次开始执行时间点之后多长时间再执行
	 */
	@Column(name = "fixed_rate", columnDefinition = "bigint not null default 0 comment '上一次开始执行时间点之后多长时间再执行'")
	private Long fixedRate;

	/**
	 * 与 fixedRate 意思相同，只是使用字符串的形式
	 */
	@Column(name = "fixed_rate_string", columnDefinition = "varchar(64) not null comment '与 fixedRate 意思相同，只是使用字符串的形式'")
	private String fixedRateString;

	/**
	 * 第一次延迟多长时间后再执行
	 */
	@Column(name = "initial_delay", columnDefinition = "bigint not null default 0 comment '第一次延迟多长时间后再执行'")
	private Long initialDelay;

	/**
	 * 与 initialDelay 意思相同，只是使用字符串的形式
	 */
	@Column(name = "initial_delay_string", columnDefinition = "varchar(64) not null comment '与 initialDelay 意思相同，只是使用字符串的形式'")
	private String initialDelayString;

	/**
	 * 任务是否已终止
	 */
	@Column(name = "cancel", columnDefinition = "boolean default false comment '任务是否已终止'")
	private boolean cancel;

	/**
	 * 执行次数
	 */
	@Column(name = "num", columnDefinition = "int default 0  comment '执行次数'")
	private int num;

	/**
	 * 方法名称
	 */
	@Column(name = "method_name", columnDefinition = "varchar(64) not null comment '方法名称'")
	private String methodName;

	/**
	 * 执行次数
	 */
	@Column(name = "bean_name", columnDefinition = "varchar(64) not null comment ' Spring Bean名称'")
	private String beanName;

	/**
	 * 类型
	 *
	 * @see ScheduledType
	 */
	@Column(name = "type", columnDefinition = "varchar(64) not null comment '类型'")
	private String type;

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		ScheduledJob that = (ScheduledJob) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
