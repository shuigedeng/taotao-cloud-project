package com.taotao.cloud.job.schedule.runner;

import com.taotao.cloud.job.schedule.task.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 启动完成后加载
 **/
@Component
public class TaskApplicationRunner implements ApplicationRunner {
	@Resource
	private TaskManager taskManager;

	private final Logger logger = LoggerFactory.getLogger(TaskApplicationRunner.class);

	@Override
	public void run(ApplicationArguments args) {
		logger.info("==== 系统运行开始 ====");

		// 初始化任务
		taskManager.initTask();
	}
}
