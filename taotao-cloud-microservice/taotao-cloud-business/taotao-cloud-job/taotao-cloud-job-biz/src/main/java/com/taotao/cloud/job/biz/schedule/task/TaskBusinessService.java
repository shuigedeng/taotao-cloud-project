package com.taotao.cloud.job.biz.schedule.task;

import java.util.Random;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("taskBusinessService")
public class TaskBusinessService {

	private final Logger logger = LoggerFactory.getLogger(TaskBusinessService.class);

	/**
	 * 为了记录异常 所有的方法必须在try内 每个方法默认接收一个taskId
	 */
	//@Async
	@SchedulerLock(name = "taskBusinessService_taskA", lockAtLeastFor = "PT30S", lockAtMostFor = "PT10M")
	public void taskA(String id) {
		try {
			logger.info("======执行业务代码 --- this is A ======");
			//模拟时长
			int number = new Random().nextInt(100) + 1;
			//模拟耗时
			Thread.sleep(2000);

			logger.info("======执行业务代码 --- this is 完成 ======");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
