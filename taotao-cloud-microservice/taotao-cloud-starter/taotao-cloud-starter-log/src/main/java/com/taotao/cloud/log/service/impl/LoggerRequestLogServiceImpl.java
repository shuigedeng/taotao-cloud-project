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
package com.taotao.cloud.log.service.impl;

import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.log.model.Log;
import com.taotao.cloud.log.service.IRequestLogService;

/**
 * 审计日志实现类-logger
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 11:18
 */
public class LoggerRequestLogServiceImpl implements IRequestLogService {

	@Override
	public void save(Log log) {
		LogUtil.info("本地日志记录成功：{}", JsonUtil.toJSONString(log));
	}
}
