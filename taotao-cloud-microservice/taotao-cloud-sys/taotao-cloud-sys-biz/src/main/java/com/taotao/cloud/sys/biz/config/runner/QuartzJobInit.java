/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.config.runner;

import com.taotao.cloud.sys.biz.service.IQuartzJobService;
import com.taotao.cloud.web.quartz.QuartzManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class QuartzJobInit implements ApplicationRunner {

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
		//List<QuartzJob> quartzJobs = IQuartzJobService.findByIsPauseIsFalse();
		//List<QuartzJobModel> quartzJobModels = new ArrayList<>();
		//
		//cn.hutool.core.bean.BeanUtil.copyProperties(quartzJobs, quartzJobModels);
		//quartzJobModels.forEach(quartzManager::addJob);
		//
		//System.out.println("--------------------定时任务注入完成---------------------");
	}
}
