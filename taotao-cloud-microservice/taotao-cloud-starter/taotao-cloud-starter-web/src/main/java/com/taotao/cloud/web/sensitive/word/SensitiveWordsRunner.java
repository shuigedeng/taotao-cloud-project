package com.taotao.cloud.web.sensitive.word;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * 敏感词加载
 */
public class SensitiveWordsRunner implements ApplicationRunner {

	@Autowired
	private RedisRepository redisRepository;

	/**
	 * 程序启动时，获取最新的需要过滤的敏感词
	 * <p>
	 * 这里即便缓存中为空也没关系，定时任务会定时重新加载敏感词
	 *
	 * @param args 启动参数
	 */
	@Override
	public void run(ApplicationArguments args) {
		Object words = redisRepository.get(RedisConstant.SENSITIVE_WORDS_KEY);
		if (Objects.nonNull(words)) {
			LogUtil.info("系统初始化敏感词");

			List<String> sensitives = (List<String>) words;
			if (sensitives.isEmpty()) {
				return;
			}
			SensitiveWordsFilter.init(sensitives);
		}
	}
}
