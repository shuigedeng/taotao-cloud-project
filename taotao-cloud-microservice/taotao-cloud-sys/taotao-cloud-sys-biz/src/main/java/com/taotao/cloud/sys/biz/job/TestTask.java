package com.taotao.cloud.sys.biz.job;

import com.taotao.cloud.web.schedule.core.ScheduledManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestTask {

	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private ScheduledManager scheduledManager;

	@Scheduled(cron = "0 */1 * * * ?")
	public void robReceiveExpireTask() {
		System.out.println(Thread.currentThread().getName() + "------------测试测试");
		System.out.println(df.format(LocalDateTime.now()) + "测试测试");

		List<String> runScheduledName = scheduledManager.getRunScheduledName();
		System.out.println(runScheduledName);

		List<String> allSuperScheduledName = scheduledManager.getAllSuperScheduledName();
		System.out.println(allSuperScheduledName);
	}
}
