/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity.scheduled;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Scheduled日志表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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
}
