package com.taotao.cloud.sys.biz.service.business.impl;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.sys.biz.model.entity.sensitive.SensitiveWord;
import com.taotao.cloud.sys.biz.mapper.ISensitiveWordMapper;
import com.taotao.cloud.sys.biz.repository.cls.SensitiveWordRepository;
import com.taotao.cloud.sys.biz.repository.inf.ISensitiveWordRepository;
import com.taotao.cloud.sys.biz.service.business.ISensitiveWordService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 敏感词业务层实现
 *
 */
@Service
public class SensitiveWordServiceImpl extends
	BaseSuperServiceImpl<ISensitiveWordMapper, SensitiveWord, SensitiveWordRepository, ISensitiveWordRepository, Long> implements
	ISensitiveWordService {

	@Autowired
	private RedisRepository redisRepository;

	@Override
	public void resetCache() {
		List<SensitiveWord> sensitiveWordsList = this.list();

		if (sensitiveWordsList == null || sensitiveWordsList.isEmpty()) {
			return;
		}

		List<String> sensitiveWords = sensitiveWordsList.stream()
			.map(SensitiveWord::getSensitiveWord).collect(Collectors.toList());

		redisRepository.set(RedisConstant.SENSITIVE_WORDS_KEY, sensitiveWords);
	}
}
