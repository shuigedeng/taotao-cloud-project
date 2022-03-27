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
package com.taotao.cloud.web.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 基于MP的 BaseMapper 新增了2个方法： insertBatchSomeColumn、updateAllById
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:17:15
 */
public interface BaseSuperMapper<T extends SuperEntity<T, I>, I extends Serializable> extends
	BaseMapper<T> {

	/**
	 * 全量修改所有字段
	 *
	 * @param entity 实体
	 * @return 修改数量
	 * @since 2021-09-02 21:17:23
	 */
	int updateAllById(@Param(Constants.ENTITY) T entity);

	/**
	 * 批量插入所有字段
	 * <p>
	 * 只测试过MySQL！只测试过MySQL！只测试过MySQL！
	 *
	 * @param entityList 实体集合
	 * @return 插入数量
	 * @since 2021-09-02 21:17:23
	 */
	int insertBatchSomeColumn(List<T> entityList);

	/**
	 * 更新数据
	 *
	 * @param id id
	 * @return 更新结果
	 * @since 2022-03-25 14:54:53
	 */
	boolean updateById(I id);
}
