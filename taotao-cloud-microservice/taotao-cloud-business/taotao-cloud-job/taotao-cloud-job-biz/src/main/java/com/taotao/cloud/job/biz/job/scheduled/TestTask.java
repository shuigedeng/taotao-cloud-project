package com.taotao.cloud.job.biz.job.scheduled;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.schedule.model.entity.Task;
import com.taotao.cloud.job.schedule.task.TaskManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:33
 */
@Component
public class TestTask {

	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private TaskManager taskManager;

	@Scheduled(cron = "0 0/30 * * * ?")
	public void robReceiveExpireTask() {
		LogUtils.info(Thread.currentThread().getName() + "------------测试测试");
		LogUtils.info(df.format(LocalDateTime.now()) + "测试测试");

		Map<String, Task> taskMap = taskManager.getTaskMap();
		LogUtils.info(taskMap.toString());

		//List<String> runScheduledName = taskManager.getRunScheduledName();
		//LogUtils.info(runScheduledName.toString());
		//
		//List<String> allSuperScheduledName = taskManager.getAllSuperScheduledName();
		//LogUtils.info(allSuperScheduledName.toString());
	}

	@Scheduled(cron = "0 */1 * * * ?")
	@SchedulerLock(name = "scheduledController_notice", lockAtLeastFor = "PT15M", lockAtMostFor = "PT14M")
	public void notice() {
		try {
			LogUtils.info(
				Thread.currentThread().getName() + "- 执行定时器 scheduledController_notice");
		} catch (Exception e) {
			LogUtils.error("异常信息:", e);
		}
	}

	/**
	 * 每分钟执行一次 [秒] [分] [小时] [日] [月] [周] [年]
	 */
	@Scheduled(cron = "1 * * * * ?")
	@SchedulerLock(name = "synchronousSchedule")
	public void synchronousSchedule() {
		System.out.println("Start run schedule to synchronous data:" + new Date());
	}
}
