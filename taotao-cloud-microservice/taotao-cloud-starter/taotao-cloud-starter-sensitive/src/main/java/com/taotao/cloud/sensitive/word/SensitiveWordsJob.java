package com.taotao.cloud.web.sensitive.word;

import static com.taotao.cloud.web.configuration.QuartzAutoConfiguration.EXECUTOR;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时更新敏感词信息
 */
public class SensitiveWordsJob extends QuartzJobBean {

	@Autowired
	private RedisRepository redisRepository;
	private static volatile Integer code = 0;

	@Override
	@SuppressWarnings("unchecked")
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		Future<Integer> submit = EXECUTOR.submit(() -> {
			Object words = redisRepository.get(RedisConstant.SENSITIVE_WORDS_KEY);
			if (Objects.nonNull(words)) {
				List<String> sensitives = (List<String>) words;
				if (sensitives.isEmpty()) {
					return -1;
				}
				int code = words.hashCode();
				LogUtil.info("敏感词更新，code={}", code);
				LogUtil.info("敏感词更新，this.code={}", SensitiveWordsJob.code);
				if (SensitiveWordsJob.code != code) {
					SensitiveWordsFilter.init(sensitives);
					return code;
				}
			}

			return -1;
		});

		try {
			Integer integer = submit.get();
			if (integer != -1) {
				SensitiveWordsJob.code = integer;
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}
}
