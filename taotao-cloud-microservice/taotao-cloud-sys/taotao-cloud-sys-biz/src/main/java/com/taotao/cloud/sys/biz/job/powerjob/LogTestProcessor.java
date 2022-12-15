//package com.taotao.cloud.sys.biz.job.powerjob;
//
//import com.alibaba.fastjson.JSONObject;
//import java.util.Date;
//import java.util.Optional;
//import org.springframework.stereotype.Component;
//import tech.powerjob.official.processors.util.CommonUtils;
//import tech.powerjob.worker.core.processor.ProcessResult;
//import tech.powerjob.worker.core.processor.TaskContext;
//import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
//import tech.powerjob.worker.log.OmsLogger;
//
///**
// * LogTestProcessor
// *
// * @author tjq
// * @since 2022/9/18
// */
//@Component
//public class LogTestProcessor implements BasicProcessor {
//
//	@Override
//	public ProcessResult process(TaskContext context) throws Exception {
//
//		final OmsLogger omsLogger = context.getOmsLogger();
//		final String parseParams = CommonUtils.parseParams(context);
//		final JSONObject config = Optional.ofNullable(JSONObject.parseObject(parseParams))
//			.orElse(new JSONObject());
//
//		final long loopTimes = Optional.ofNullable(config.getLong("loopTimes")).orElse(1000L);
//
//		for (int i = 0; i < loopTimes; i++) {
//			omsLogger.debug("[DEBUG] one DEBUG log in {}", new Date());
//			omsLogger.info("[INFO] one INFO log in {}", new Date());
//			omsLogger.warn("[WARN] one WARN log in {}", new Date());
//			omsLogger.error("[ERROR] one ERROR log in {}", new Date());
//		}
//
//		return new ProcessResult(true);
//	}
//}
