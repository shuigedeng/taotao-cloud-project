package com.taotao.cloud.job.biz.schedule.runner;

import com.taotao.cloud.job.schedule.task.TaskManager;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * 启动完成后加载
 **/
@Component
public class ScheduleApplicationDestroy implements DisposableBean {

	private final Logger logger = LoggerFactory.getLogger(ScheduleApplicationRunner.class);

	@Resource
	private TaskManager taskManager;

	@Override
	public void destroy() throws Exception {
		logger.info("==== 系统运行结束 ====");
		taskManager.destroyTask();
	}

}
