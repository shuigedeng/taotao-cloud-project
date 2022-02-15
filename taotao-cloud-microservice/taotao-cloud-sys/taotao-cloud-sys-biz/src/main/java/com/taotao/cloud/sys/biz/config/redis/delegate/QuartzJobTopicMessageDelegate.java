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
package com.taotao.cloud.sys.biz.config.redis.delegate;

import com.taotao.cloud.sys.biz.service.IQuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * QuartzJobTopicMessageDelegate
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2022/02/09 20:45
 */
@Component
public class QuartzJobTopicMessageDelegate {
	@Autowired
	private IQuartzJobService IQuartzJobService;

	public void addJob(String message) {
	}

	public void deleteJob(String message) {
	}

	public void resumeJob(String message) {
	}

	public void pauseJob(String message) {
	}

	public void runJobNow(String message) {
	}

	public void updateJob(String message) {
	}

	public void addJobLog(String message) {
	}
}
