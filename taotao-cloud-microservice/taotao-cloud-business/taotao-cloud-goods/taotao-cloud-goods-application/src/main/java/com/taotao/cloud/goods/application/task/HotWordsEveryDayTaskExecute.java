/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.goods.application.task;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.job.xxl.timetask.EveryDayExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 热点词汇每天任务执行
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:54:14
 */
@Component
public class HotWordsEveryDayTaskExecute implements EveryDayExecute {

	/**
	 * 复述,库
	 */
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
