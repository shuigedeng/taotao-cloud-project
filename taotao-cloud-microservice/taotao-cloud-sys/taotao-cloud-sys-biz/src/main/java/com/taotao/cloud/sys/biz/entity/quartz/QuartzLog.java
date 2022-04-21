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
package com.taotao.cloud.sys.biz.entity.quartz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * Quartz日志表
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
@Table(name = QuartzLog.TABLE_NAME)
@TableName(QuartzLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = QuartzLog.TABLE_NAME, comment = "Quartz日志表")
public class QuartzLog extends BaseSuperEntity<QuartzLog, Long> {

	public static final String TABLE_NAME = "tt_quartz_log";

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
	 * 异常详细
	 */
	@Column(name = "exception_detail", columnDefinition = "varchar(4096) not null comment 'cron 异常详细'")
	private String exceptionDetail;

	/**
	 * 状态
	 */
	@Column(name = "is_success", columnDefinition = "boolean DEFAULT false comment '状态'")
	private Boolean isSuccess;

	/**
	 * 任务名称
	 */
	@Column(name = "job_name", columnDefinition = "varchar(64) not null comment '任务名称'")
	private String jobName;

	/**
	 * 方法名称
	 */
	@Column(name = "method_name", columnDefinition = "varchar(64) not null comment '方法名称'")
	private String methodName;

	/**
	 * 参数
	 */
	@Column(name = "params", columnDefinition = "varchar(64) not null comment '参数'")
	private String params;

	/**
	 * 耗时（毫秒）
	 */
	@Column(name = "time", columnDefinition = "bigint not null default 1 comment '耗时（毫秒）'")
	private Long time;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		QuartzLog quartzLog = (QuartzLog) o;
		return getId() != null && Objects.equals(getId(), quartzLog.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
