package com.taotao.cloud.job.biz.powerjob.processors;

import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * 测试超时任务
 */
@Component
public class TimeoutProcessor implements BasicProcessor {

	@Override
	public ProcessResult process(TaskContext context) throws Exception {
		Thread.sleep(Long.parseLong(context.getJobParams()));
		return new ProcessResult(true, "impossible~~~~QAQ~");
	}
}
