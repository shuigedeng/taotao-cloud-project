package com.taotao.cloud.job.schedule.runner;

import com.taotao.cloud.job.schedule.task.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 启动完成后加载
 **/
@Component
public class TaskApplicationDestroy implements DisposableBean {

	private final Logger logger = LoggerFactory.getLogger(TaskApplicationRunner.class);

	@Resource
	private TaskManager taskManager;

	@Override
	public void destroy() throws Exception {
		logger.info("==== 系统运行结束 ====");
		taskManager.destroyTask();
	}

}
