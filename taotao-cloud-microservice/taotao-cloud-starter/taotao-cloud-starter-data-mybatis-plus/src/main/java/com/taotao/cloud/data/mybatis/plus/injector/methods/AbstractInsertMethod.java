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

package com.taotao.cloud.data.mybatis.plus.injector.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.taotao.cloud.data.mybatis.plus.injector.MateSqlMethod;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 抽象的 插入一条数据（选择字段插入）
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:41:29
 */
public class AbstractInsertMethod extends AbstractMethod {

	private final MateSqlMethod sqlMethod;

	public AbstractInsertMethod(MateSqlMethod sqlMethod) {
		this.sqlMethod = sqlMethod;
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass,
		TableInfo tableInfo) {
		KeyGenerator keyGenerator = new NoKeyGenerator();
		String columnScript = SqlScriptUtils.convertTrim(
			tableInfo.getAllInsertSqlColumnMaybeIf(null),
			LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
		String valuesScript = SqlScriptUtils.convertTrim(
			tableInfo.getAllInsertSqlPropertyMaybeIf(null),
			LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
		String keyProperty = null;
		String keyColumn = null;
		// 表包含主键处理逻辑,如果不包含主键当普通字段处理
		if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
			if (tableInfo.getIdType() == IdType.AUTO) {
				// 自增主键
				keyGenerator = new Jdbc3KeyGenerator();
				keyProperty = tableInfo.getKeyProperty();
				keyColumn = tableInfo.getKeyColumn();
			} else {
				if (null != tableInfo.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(sqlMethod.getMethod(), tableInfo,
						builderAssistant);
					keyProperty = tableInfo.getKeyProperty();
					keyColumn = tableInfo.getKeyColumn();
				}
			}
		}
		String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript,
			valuesScript);
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(),
			sqlSource, keyGenerator, keyProperty, keyColumn);
	}
}
