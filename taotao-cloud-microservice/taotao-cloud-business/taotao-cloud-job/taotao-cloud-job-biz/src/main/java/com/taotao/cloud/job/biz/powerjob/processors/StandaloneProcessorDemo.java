package com.taotao.cloud.job.biz.powerjob.processors;

import java.util.Collections;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

/**
 * 单机处理器 示例
 */
@Component
public class StandaloneProcessorDemo implements BasicProcessor {

	@Override
	public ProcessResult process(TaskContext context) throws Exception {

		OmsLogger omsLogger = context.getOmsLogger();
		omsLogger.info("StandaloneProcessorDemo start process,context is {}.", context);
		omsLogger.info(
			"Notice! If you want this job process failed, your jobParams need to be 'failed'");

		omsLogger.info("Let's test the exception~");
		// 测试异常日志
		try {
			Collections.emptyList().add("277");
		} catch (Exception e) {
			omsLogger.error("oh~it seems that we have an exception~", e);
		}

		System.out.println("================ StandaloneProcessorDemo#process ================");
		System.out.println(context.getJobParams());
		// 根据控制台参数判断是否成功
		boolean success = !"failed".equals(context.getJobParams());
		omsLogger.info("StandaloneProcessorDemo finished process,success: .", success);

		omsLogger.info("anyway, we finished the job successfully~Congratulations!");
		return new ProcessResult(success, context + ": " + success);
	}
}
