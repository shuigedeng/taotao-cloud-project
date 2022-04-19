/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
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
 * Quartz任务表
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
@Table(name = QuartzJob.TABLE_NAME)
@TableName(QuartzJob.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = QuartzJob.TABLE_NAME, comment = "Quartz任务表")
public class QuartzJob extends BaseSuperEntity<QuartzJob, Long> {

	public static final String TABLE_NAME = "tt_quartz_job";

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
	 * 状态：1暂停、0启用
	 */
	@Column(name = "is_pause", columnDefinition = "boolean DEFAULT false comment '收件人'")
	private Boolean isPause;

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
	 * 备注
	 */
	@Column(name = "remark", columnDefinition = "varchar(256) not null comment '备注'")
	private String remark;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		QuartzJob quartzJob = (QuartzJob) o;
		return getId() != null && Objects.equals(getId(), quartzJob.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
