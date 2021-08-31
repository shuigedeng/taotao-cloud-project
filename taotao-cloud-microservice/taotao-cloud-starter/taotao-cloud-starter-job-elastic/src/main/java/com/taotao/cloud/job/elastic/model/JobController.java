/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.job.elastic.model;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * JobController
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 21:08
 */
@RestController
public class JobController {

	private JobService jobService;

	public JobController(JobService jobService) {
		this.jobService = jobService;
	}

	/**
	 * 添加动态任务（适用于脚本逻辑已存在的情况，只是动态添加了触发的时间）
	 *
	 * @param job 任务信息
	 */
	@PostMapping("/job")
	public Object addJob(@RequestBody Job job) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", true);

		if (!StringUtils.hasText(job.getJobName())) {
			result.put("status", false);
			result.put("message", "name not null");
			return result;
		}

		if (!StringUtils.hasText(job.getCron())) {
			result.put("status", false);
			result.put("message", "cron not null");
			return result;
		}

		if (!StringUtils.hasText(job.getJobType())) {
			result.put("status", false);
			result.put("message", "getJobType not null");
			return result;
		}

		if ("SCRIPT".equals(job.getJobType())) {
			if (!StringUtils.hasText(job.getScriptCommandLine())) {
				result.put("status", false);
				result.put("message", "scriptCommandLine not null");
				return result;
			}
		} else {
			if (!StringUtils.hasText(job.getJobClass())) {
				result.put("status", false);
				result.put("message", "jobClass not null");
				return result;
			}
		}

		try {
			jobService.addJob(job);
		} catch (Exception e) {
			result.put("status", false);
			result.put("message", e.getMessage());
		}
		return result;
	}

	/**
	 * 删除动态注册的任务（只删除注册中心中的任务信息）
	 *
	 * @param jobName 任务名称
	 */
	@GetMapping("/job/remove")
	public Object removeJob(String jobName) {
		Map<String, Object> result = new HashMap<>();
		result.put("status", true);
		try {
			jobService.removeJob(jobName);
		} catch (Exception e) {
			result.put("status", false);
			result.put("message", e.getMessage());
		}
		return result;
	}
}
