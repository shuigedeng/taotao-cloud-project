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

package com.taotao.cloud.job.biz.quartz.dao.dataobject;

import com.art.common.mp.core.base.BaseCreateEntity;
import com.art.common.quartz.core.constants.ScheduleConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 定时任务调度表
 *
 * @author fxz
 * @date 2022-04-03
 */
@Data
@TableName("sys_job")
public class JobDO extends BaseCreateEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 任务ID
	 */
	@TableId(type = IdType.AUTO)
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

}
