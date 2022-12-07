package com.taotao.cloud.data.sync.batch.multi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @Author : JCccc
 * @CreateTime : 2020/3/17
 * @Description :监听Job执行情况，实现JobExecutorListener，且在batch配置类里，Job的Bean上绑定该监听器
 **/

public class MyJobListener implements JobExecutionListener {

	private Logger logger = LoggerFactory.getLogger(MyJobListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("job 开始, id={}", jobExecution.getJobId());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		logger.info("job 结束, id={}", jobExecution.getJobId());
	}
}
