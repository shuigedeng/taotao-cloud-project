package com.taotao.cloud.goods.biz.timetask;

import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.web.timetask.EveryDayExecute;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@Slf4j
@Component
public class HotWordsEveryDayTaskExecute implements EveryDayExecute {

	@Autowired
	private RedisRepository redisRepository;

	/**
	 * 执行每日任务
	 */
	@Override
	public void execute() {
		//移除昨日的热搜词
		redisRepository.del(CachePrefix.HOT_WORD.getPrefix());
	}

}
