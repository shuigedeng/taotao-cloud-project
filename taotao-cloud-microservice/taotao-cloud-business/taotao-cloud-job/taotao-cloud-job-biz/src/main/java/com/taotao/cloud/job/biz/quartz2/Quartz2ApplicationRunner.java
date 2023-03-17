package com.taotao.cloud.job.biz.quartz2;

import com.taotao.cloud.job.biz.quartz2.service.JobService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动完成后加载
 **/
@Component
public class Quartz2ApplicationRunner implements ApplicationRunner {

	@Resource
	private JobService jobService;

	private final Logger logger = LoggerFactory.getLogger(Quartz2ApplicationRunner.class);

	@Override
	public void run(ApplicationArguments args) {
		logger.info("==== Quartz2ApplicationRunner 系统运行开始 ====");

		// 初始化任务
		jobService.initTask();
	}
}
