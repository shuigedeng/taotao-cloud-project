package com.taotao.cloud.web.sensitive.word;

import static com.taotao.cloud.web.configuration.QuartzConfiguration.EXECUTOR;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.List;
import java.util.Objects;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 间隔更新敏感词
 */
public class SensitiveWordsJob extends QuartzJobBean {

	@Autowired
	private RedisRepository redisRepository;
	private int code = 0;

	/**
	 * 定时更新敏感词信息
	 */
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		EXECUTOR.execute(() -> {
			Object words = redisRepository.get(RedisConstant.SENSITIVE_WORDS_KEY);
			if (Objects.nonNull(words)) {
				List<String> sensitives = (List<String>) words;
				if (sensitives.isEmpty()) {
					return;
				}
				int code = words.hashCode();
				LogUtil.info("敏感词更新，code={}", code);
				LogUtil.info("敏感词更新，this.code={}", this.code);
				if (this.code != code) {
					SensitiveWordsFilter.init(sensitives);
					this.code = code;
				}
			}
		});
	}
}
