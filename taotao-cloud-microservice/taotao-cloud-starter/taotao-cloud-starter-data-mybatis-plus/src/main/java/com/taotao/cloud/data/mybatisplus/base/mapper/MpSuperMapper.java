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
package com.taotao.cloud.data.mybatisplus.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.data.mybatisplus.base.entity.MpSuperEntity;
import com.taotao.cloud.data.mybatisplus.query.BaseMapperX;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 基于MP的 BaseMapper 新增了2个方法： insertBatchSomeColumn、updateAllById
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:17:15
 */
public interface MpSuperMapper<T extends MpSuperEntity<I>, I extends Serializable> extends
	BaseMapperX<T> {

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
	 * 自定义批量插入
	 * 如果要自动填充，@Param(xx) xx参数名必须是 list/collection/array 3个的其中之一
	 */
	int insertBatch(@Param("list") List<T> list);

	/**
	 * 自定义批量更新，条件为主键
	 * 如果要自动填充，@Param(xx) xx参数名必须是 list/collection/array 3个的其中之一
	 */
	//int updateBatch(@Param("list") List<T> list);

}
