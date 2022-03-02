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

import com.taotao.cloud.sys.biz.service.ICronService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.scheduling.support.CronSequenceGenerator;
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
		CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(expression);
		Date current = new Date();
		for (int i = 0; i < 10; i++) {
			current = cronSequenceGenerator.next(current);
			nextTimes.add(DateFormatUtils.format(current,"yyyy-MM-dd HH:mm:ss"));
		}

		return nextTimes;
	}
}
