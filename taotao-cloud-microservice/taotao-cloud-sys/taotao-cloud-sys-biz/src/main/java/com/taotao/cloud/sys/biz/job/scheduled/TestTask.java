package com.taotao.cloud.sys.biz.job.scheduled;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

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

	// @Autowired
	// private TaskManager taskManager;
	//
	// @Scheduled(cron = "0 0/30 * * * ?")
	// public void robReceiveExpireTask() {
	// 	LogUtils.info(Thread.currentThread().getName() + "------------测试测试");
	// 	LogUtils.info(df.format(LocalDateTime.now()) + "测试测试");
	//
	// 	List<String> runScheduledName = taskManager.getRunScheduledName();
	// 	LogUtils.info(runScheduledName.toString());
	//
	// 	List<String> allSuperScheduledName = taskManager.getAllSuperScheduledName();
	// 	LogUtils.info(allSuperScheduledName.toString());
	// }
}
