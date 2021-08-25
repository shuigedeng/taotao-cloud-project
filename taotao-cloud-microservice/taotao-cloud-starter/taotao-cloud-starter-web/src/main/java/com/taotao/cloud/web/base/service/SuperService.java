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
package com.taotao.cloud.web.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.web.base.mapper.SuperMapper;
import java.util.List;

/**
 * 基于MP的 IService 新增了2个方法： saveBatchSomeColumn、updateAllById 其中： 1，updateAllById 执行后，会清除缓存
 * 2，saveBatchSomeColumn 批量插入
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 08:19
 */
public interface SuperService<T> extends IService<T> {

	@Override
	Class<T> getEntityClass();

	/**
	 * 批量保存数据
	 * <p>
	 * 注意：该方法仅仅测试过mysql
	 *
	 * @param entityList entityList
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021/8/25 08:20
	 */
	default boolean saveBatchSomeColumn(List<T> entityList) {
		if (entityList.isEmpty()) {
			return true;
		}
		if (entityList.size() > 5000) {
			throw new BusinessException("太多数据啦");
		}
		return SqlHelper.retBool(((SuperMapper) getBaseMapper()).insertBatchSomeColumn(entityList));
	}

	/**
	 * 根据id修改 entity 的所有字段
	 *
	 * @param entity entity
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021/8/25 08:20
	 */
	boolean updateAllById(T entity);

}
