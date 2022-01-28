package com.taotao.cloud.web.sensitive.word;

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

	/**
	 * 定时更新敏感词信息
	 *
	 * @param jobExecutionContext
	 */
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) {
		Object words = redisRepository.get(RedisConstant.SENSITIVE_WORDS_KEY);
		if (Objects.nonNull(words)) {
			LogUtil.info("敏感词定时更新");

			List<String> sensitives = (List<String>) words;
			if (sensitives.isEmpty()) {
				return;
			}
			SensitiveWordsFilter.init(sensitives);
		}
	}
}
