/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.biz.quartz.core.dto;

import com.taotao.cloud.job.quartz.quartz.core.constants.ScheduleConstants;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 21:49
 */
@Data
//public class JobPageDTO extends BasePageEntity implements Serializable {
public class JobPageDTO implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 任务ID
	 */
	private Long jobId;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 任务组名
	 */
	private String jobGroup;

	/**
	 * 执行参数
	 */
	private String parameters;

	/**
	 * cron执行表达式
	 */
	private String cronExpression;

	/**
	 * cron计划策略
	 */
	private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

	/**
	 * 任务状态（0正常 1暂停）
	 */
	private String status;

	/**
	 * 备注信息
	 */
	private String remark;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	public long getCurrent() {
		return 1;
	}

	public long getSize() {
		return 1;
	}
}
