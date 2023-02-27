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
package com.taotao.cloud.job.biz.init;

import com.taotao.cloud.job.quartz.utils.QuartzManager;
import com.taotao.cloud.sys.biz.service.business.IQuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 计划工作初始化
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:00
 */
@Component
public class ScheduledJobInit implements ApplicationRunner {

	@Autowired
	private IQuartzJobService quartzJobService;
	@Autowired
	private QuartzManager quartzManager;

	/**
	 * 项目启动时重新激活启用的定时任务
	 *
	 * @param applicationArguments /
	 */
	@Override
	public void run(ApplicationArguments applicationArguments) {
		//System.out.println("--------------------注入定时任务---------------------");
		//List<QuartzJob> quartzJobs = quartzJobService.findByIsPauseIsFalse();
		//List<QuartzJobModel> quartzJobModels = new ArrayList<>();
		//
		//cn.hutool.core.bean.BeanUtil.copyProperties(quartzJobs, quartzJobModels);
		//quartzJobModels.forEach(quartzManager::addJob);
		//
		//System.out.println("--------------------定时任务注入完成---------------------");
	}
}
