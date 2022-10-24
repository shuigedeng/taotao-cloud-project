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
package com.taotao.cloud.data.p6spy.logger.format;

import cn.hutool.db.sql.SqlFormatter;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * P6spy SQL 日志格式化
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/14 09:37
 */
public class P6spyLogFormat implements MessageFormattingStrategy {

	/**
	 * @param connectionId 连接id
	 * @param now 当前时间
	 * @param elapsed 执行时长，包括执行 SQL 和处理结果集的时间(可以参考来调优)
	 * @param category 语句分类，statement、resultset 等
	 * @param prepared 查询语句。可能是 prepared statement，表现为 select * from table1 where c1=?，问号参数形式
	 * @param sql 含参数值的查询语句，如 select * from from table1 where c1=7
	 */
	@Override
	public String formatMessage(final int connectionId, final String now, final long elapsed,
		final String category, final String prepared, final String sql, final String url) {

		return """
			    Consume Time: %s  Now: %s connectionId: %s category: %s
			    Execute SQL：%s
			""".formatted(elapsed, now, connectionId, category,
			SqlFormatter.format(sql.replaceAll("[\\s]+", " ")));

		//return StringUtils.isNotBlank(sql) ? " Consume Time：" + elapsed
		//	+ " ms " + now +
		//	"\n Execute SQL：" + SqlFormatter.format(sql.replaceAll("[\\s]+", " ")) + "\n" : "";
		//
		//return !"".equals(sql.trim()) ?
		//	LocalDateTime.now().format(CommonConstant.DATETIME_FORMATTER) + " | took " + elapsed
		//		+ "ms | " + category + " | connection " + connectionId + "\n " + sql + ";" : "";
	}
}
