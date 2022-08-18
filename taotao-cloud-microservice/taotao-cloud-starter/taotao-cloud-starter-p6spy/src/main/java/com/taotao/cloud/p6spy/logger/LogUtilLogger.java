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
package com.taotao.cloud.p6spy.logger;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.FormattedLogger;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;

/**
 * P6spy日志实现
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/14 09:38
 */
public class LogUtilLogger extends FormattedLogger {

	@Override
	public void logException(Exception e) {
		LogUtil.error(e,"数据库日志错误");
	}

	@Override
	public void logText(String text) {
	}

	/**
	 * 重写输出方法
	 * @param connectionId 连接id
	 * @param now 当前时间
	 * @param elapsed 执行时长，包括执行 SQL 和处理结果集的时间(可以参考来调优)
	 * @param category 语句分类，statement、resultset 等
	 * @param prepared 查询语句。可能是 prepared statement，表现为 select * from table1 where c1=?，问号参数形式
	 * @param sql 含参数值的查询语句，如 select * from from table1 where c1=7
	 */
	@Override
	public void logSQL(int connectionId, String now, long elapsed, Category category,
		String prepared, String sql, String url) {
		final String msg = strategy.formatMessage(connectionId, now, elapsed,
			category.toString(), prepared, sql, url);

		if (StringUtil.isEmpty(msg)) {
			return;
		}

		if (Category.WARN.equals(category)) {
			LogUtil.warn(msg);
		} else if (Category.DEBUG.equals(category)) {
			LogUtil.debug(msg);
		} else {
			LogUtil.info(msg);
		}
	}

	@Override
	public boolean isCategoryEnabled(Category category) {
		return true;
	}
}
