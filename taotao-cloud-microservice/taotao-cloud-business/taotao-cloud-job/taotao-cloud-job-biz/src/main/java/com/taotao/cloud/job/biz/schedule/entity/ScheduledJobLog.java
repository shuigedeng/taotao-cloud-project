package com.taotao.cloud.job.biz.schedule.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ScheduledJobLog.TABLE_NAME)
@TableName(ScheduledJobLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ScheduledJobLog.TABLE_NAME, comment = "Scheduled任务日志表")
public class ScheduledJobLog extends BaseSuperEntity<ScheduledJobLog, String> {

	public static final String TABLE_NAME = "tt_scheduled_job_log";

	@Column(name = "task_id", columnDefinition = "varchar(64) not null comment '任务名'")
	private String taskId;

	@Column(name = "time", columnDefinition = "varchar(125) not null comment '任务名'")
	private String time;

	//执行状态（0正常 1失败）
	@Column(name = "status", columnDefinition = "int not null default 2 comment '执行状态（0正常 1失败）'")
	private Integer status;

	@Column(name = "exception_info", columnDefinition = "text null comment '异常信息'")
	private String exceptionInfo;

}
