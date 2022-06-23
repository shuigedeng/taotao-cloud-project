package com.taotao.cloud.sys.biz.task.scheduled;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.schedule.common.annotation.ScheduledBean;
import com.taotao.cloud.web.schedule.core.ScheduledManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
	private ScheduledManager scheduledManager;

	@ScheduledBean(cron = "0 0/30 * * * ?")
	public void robReceiveExpireTask() {
		LogUtil.info(Thread.currentThread().getName() + "------------测试测试");
		LogUtil.info(df.format(LocalDateTime.now()) + "测试测试");

		List<String> runScheduledName = scheduledManager.getRunScheduledName();
		LogUtil.info(runScheduledName.toString());

		List<String> allSuperScheduledName = scheduledManager.getAllSuperScheduledName();
		LogUtil.info(allSuperScheduledName.toString());
	}
}
