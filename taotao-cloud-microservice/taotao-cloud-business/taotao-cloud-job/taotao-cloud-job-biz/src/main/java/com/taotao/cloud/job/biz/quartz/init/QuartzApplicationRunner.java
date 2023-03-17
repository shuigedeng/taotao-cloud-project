package com.taotao.cloud.job.biz.quartz.init;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.biz.quartz.service.QuartzJobService;
import jakarta.annotation.Resource;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动完成后加载
 **/
@Component
public class QuartzApplicationRunner implements ApplicationRunner {

	@Resource
	private QuartzJobService quartzJobService;

	private final Logger logger = LoggerFactory.getLogger(QuartzApplicationRunner.class);

	@Override
	public void run(ApplicationArguments args) {
		logger.info("==== Quartz1ApplicationRunner 系统运行开始 ====");

		// 初始化任务
		try {
			quartzJobService.init();
		} catch (SchedulerException e) {
			LogUtils.error(e);
			throw new RuntimeException(e);
		}
	}
}
