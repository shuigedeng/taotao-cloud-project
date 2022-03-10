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
package com.taotao.cloud.data.mybatis.plus.interceptor;

import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.model.Collector;
import java.util.Properties;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * SqlMybatisInterceptor
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:10:37
 */
@Intercepts({
	@Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class}),
	@Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
	@Signature(method = "update", type = Executor.class, args = {MappedStatement.class,
		Object.class})})
public class SqlMybatisInterceptor implements Interceptor {

	private final Collector collector;

	public SqlMybatisInterceptor(Collector collector) {
		this.collector = collector;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		// sql语句
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}

		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		String sql = boundSql.getSql();

		return collector.hook("taotao.cloud.health.mybatis.sql.hook").run(
			StringUtil.nullToEmpty(sql).replace("\r", "").replace("\n", ""), () -> {
				try {
					return invocation.proceed();
				} catch (Exception e) {
					LogUtil.error(e);
					throw new RuntimeException(e);
				}
			});
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

}
