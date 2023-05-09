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

package com.taotao.cloud.job.biz.schedule.service;

import com.taotao.cloud.job.biz.schedule.entity.ScheduledJob;
import com.taotao.cloud.job.biz.schedule.entity.ScheduledJobLog;
import com.taotao.cloud.job.biz.schedule.model.ScheduledJobDTO;
import com.taotao.cloud.job.biz.schedule.model.ScheduledJobVO;

import java.util.List;

/**
 * 计划工作服务
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:10:03
 */
public interface ScheduledJobService {

	/***
	 *任务列表查询
	 * @return o
	 */
	List<ScheduledJob> jobList();

	/**
	 * 新增任务
	 *
	 * @param param 新增参数
	 * @return Boolean
	 */
	boolean addTask(ScheduledJobDTO param);

	/**
	 * 修改任务
	 *
	 * @param param 修改参数
	 * @return Boolean
	 */
	boolean updateTask(ScheduledJobDTO param);

	/**
	 * 执行任务
	 *
	 * @param id 任务id
	 * @return
	 */
	boolean invokeTask(String id);

	/**
	 * 暂停任务
	 *
	 * @param id 任务id
	 */
	boolean stopTask(String id);

	/**
	 * 删除任务
	 *
	 * @param id 任务id
	 */
	boolean deleteTask(String id);

	/**
	 * 禁用任务
	 *
	 * @param id 任务id
	 */
	boolean forbidTask(String id);

	/**
	 * 查询详情
	 *
	 * @param id 任务id
	 */
	ScheduledJobVO getTaskById(String id);

	/**
	 * 任务日志
	 */
	void insertTaskLog(ScheduledJobLog log);
}
