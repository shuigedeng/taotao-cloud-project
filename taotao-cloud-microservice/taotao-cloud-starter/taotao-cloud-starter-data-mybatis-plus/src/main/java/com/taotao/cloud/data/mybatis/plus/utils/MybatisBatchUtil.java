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
package com.taotao.cloud.data.mybatis.plus.utils;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MybatisBatchUtil
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2022/03/24 14:18
 */
public class MybatisBatchUtil {

	/**
	 * 默认每次处理1000条
	 */
	private static final int BATCH_SIZE = 1000;

	/**
	 * 批量处理修改或者插入
	 *
	 * @param data        处理的数据
	 * @param mapperClass mapper类
	 * @param function    function 处理逻辑
	 * @return 影响的总行数
	 * @since 2022-03-24 14:29:12
	 */
	public <T, U, R> int batchUpdateOrInsert(List<T> data, Class<U> mapperClass,
		BiFunction<T, U, R> function) {
		int i = 1;
		SqlSessionFactory sqlSessionFactory = ContextUtil.getBean(SqlSessionFactory.class, false);
		if (Objects.isNull(sqlSessionFactory)) {
			throw new MybatisPlusException("未获取到sqlSession");
		}

		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
		try {
			U mapper = sqlSession.getMapper(mapperClass);
			int size = data.size();
			for (T element : data) {
				function.apply(element, mapper);
				if ((i % BATCH_SIZE == 0) || i == size) {
					sqlSession.flushStatements();
				}
				i++;
			}
		} catch (Exception e) {
			sqlSession.rollback();
			LogUtil.error(e);
		} finally {
			sqlSession.close();
		}

		return i - 1;
	}
}
