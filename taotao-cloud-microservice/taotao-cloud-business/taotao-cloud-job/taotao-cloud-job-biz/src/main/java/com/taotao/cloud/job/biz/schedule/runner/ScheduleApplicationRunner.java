package com.taotao.cloud.job.biz.schedule.runner;

import com.taotao.cloud.job.schedule.task.TaskManager;
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
public class ScheduleApplicationRunner implements ApplicationRunner {

	@Resource
	private TaskManager taskManager;

	private final Logger logger = LoggerFactory.getLogger(ScheduleApplicationRunner.class);

	@Override
	public void run(ApplicationArguments args) {
		logger.info("==== 系统运行开始 ====");

		// 初始化任务
		taskManager.initTask();
	}
}
