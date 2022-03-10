/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.sys.biz.service.ICronService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

/**
 * CronServiceImpl
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2022/03/02 15:43
 */
@Service
public class CronServiceImpl implements ICronService {

	@Override
	public List<String> cronNextExecutionTime(String expression) {
		List<String> nextTimes = new ArrayList<>();
		CronExpression cronSequenceGenerator = CronExpression.parse(expression);
		LocalDateTime current = LocalDateTime.now();
		for (int i = 0; i < 10; i++) {
			current = cronSequenceGenerator.next(current);
			nextTimes.add(current.format(CommonConstant.DATETIME_FORMATTER));
		}

		return nextTimes;
	}
}
