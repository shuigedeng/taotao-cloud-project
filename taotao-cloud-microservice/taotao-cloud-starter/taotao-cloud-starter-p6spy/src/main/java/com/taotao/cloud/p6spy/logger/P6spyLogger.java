/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.p6spy.logger;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.FormattedLogger;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import org.springframework.util.StringUtils;
/**
 * P6spy日志实现
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/14 09:38
 */
public class P6spyLogger extends FormattedLogger {

	private Exception e;

	@Override
	public void logException(Exception e) {
		this.e = e;
		LogUtil.error("数据库日志错误", e);
	}

	@Override
	public void logText(String text) {
		LogUtil.info(text);
	}

	@Override
	public void logSQL(int connectionId, String now, long elapsed, Category category,
		String prepared, String sql, String url) {
		final String msg = strategy.formatMessage(connectionId, now, elapsed,
			category.toString(), prepared, sql, url);

		if (StringUtil.isEmpty(msg)) {
			return;
		}
		if (Category.ERROR.equals(category)) {
			LogUtil.error(msg, e);
		} else if (Category.WARN.equals(category)) {
			LogUtil.warn(msg);
		} else if (Category.DEBUG.equals(category)) {
			LogUtil.debug(msg);
		} else {
			LogUtil.info(msg);
		}
	}

	@Override
	public boolean isCategoryEnabled(Category category) {
		if (Category.ERROR.equals(category)) {
			return LogUtil.isErrorEnabled();
		} else if (Category.WARN.equals(category)) {
			return LogUtil.isWarnEnabled();
		} else if (Category.DEBUG.equals(category)) {
			return LogUtil.isDebugEnabled();
		} else {
			return LogUtil.isInfoEnabled();
		}
	}
}
