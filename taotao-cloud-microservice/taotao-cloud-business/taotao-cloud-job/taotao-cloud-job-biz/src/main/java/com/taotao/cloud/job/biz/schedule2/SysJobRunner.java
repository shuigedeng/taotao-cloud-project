//package com.taotao.cloud.job.biz.schedule2;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.taotao.cloud.common.utils.collection.CollectionUtils;
//import com.taotao.cloud.job.schedule.schedule2.CronTaskRegistrar;
//import com.taotao.cloud.job.schedule.schedule2.ScheduleSetting;
//import com.taotao.cloud.job.schedule.schedule2.SchedulingRunnable;
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SysJobRunner implements CommandLineRunner {
//
//	private static final Logger logger = LoggerFactory.getLogger(SysJobRunner.class);
//
//	@Autowired
//	private CronTaskRegistrar cronTaskRegistrar;
//
//	@Override
//	public void run(String... args) {
//		// 初始加载数据库里状态为正常的定时任务
//		ScheduleSetting existedSysJob = new ScheduleSetting();
//		List<ScheduleSetting> jobList = existedSysJob.selectList(
//			new QueryWrapper<ScheduleSetting>().eq("job_status", 1));
//		if (CollectionUtils.isNotEmpty(jobList)) {
//			for (ScheduleSetting job : jobList) {
//				SchedulingRunnable task = new SchedulingRunnable(job.getBeanName(),
//					job.getMethodName(), job.getMethodParams());
//				cronTaskRegistrar.addCronTask(task, job.getCronExpression());
//			}
//			logger.info("定时任务已加载完毕...");
//		}
//	}
//}
