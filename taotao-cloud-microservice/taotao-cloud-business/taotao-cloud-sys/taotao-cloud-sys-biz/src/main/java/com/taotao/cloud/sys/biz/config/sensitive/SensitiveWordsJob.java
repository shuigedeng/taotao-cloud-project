package com.taotao.cloud.sys.biz.config.sensitive;


import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 定时更新敏感词信息
 */
public class SensitiveWordsJob extends QuartzJobBean {

	//@Autowired
	//private RedisRepository redisRepository;
	//private static volatile Integer code = 0;

	@Override
	@SuppressWarnings("unchecked")
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		//Future<Integer> submit = EXECUTOR.submit(() -> {
		//	Object words = redisRepository.get(RedisConstant.SENSITIVE_WORDS_KEY);
		//	if (Objects.nonNull(words)) {
		//		List<String> sensitives = (List<String>) words;
		//		if (sensitives.isEmpty()) {
		//			return -1;
		//		}
		//		int code = words.hashCode();
		//		LogUtils.info("敏感词更新，code={}", code);
		//		LogUtils.info("敏感词更新，this.code={}", SensitiveWordsJob.code);
		//		if (SensitiveWordsJob.code != code) {
		//			SensitiveWordsFilter.init(sensitives);
		//			return code;
		//		}
		//	}
		//
		//	return -1;
		//});
		//
		//try {
		//	Integer integer = submit.get();
		//	if (integer != -1) {
		//		SensitiveWordsJob.code = integer;
		//	}
		//} catch (InterruptedException | ExecutionException e) {
		//	e.printStackTrace();
		//}

	}
}
