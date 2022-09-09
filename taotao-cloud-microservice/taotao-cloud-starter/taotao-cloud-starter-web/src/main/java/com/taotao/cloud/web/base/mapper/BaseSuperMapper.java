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
package com.taotao.cloud.web.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.exception.ExceptionUtils;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
	 * 选择页面
	 *
	 * @param queryWrapper 查询包装
	 * @param pageParam    页面参数
	 * @return {@link IPage }<{@link T }>
	 * @since 2022-09-07 08:52:08
	 */
	default IPage<T> selectPage(@Param("ew") Wrapper<T> queryWrapper, PageParam pageParam) {
		// MyBatis Plus 查询
		IPage<T> page = pageParam.buildMpPage();
		return selectPage(page, queryWrapper);
	}

	// default IPage<T> selectPage(@Param("ew") Wrapper<T> queryWrapper,
	// 	Collection<SortingField> sortingFields) {
	// 	// MyBatis Plus 查询
	// 	Page<T> mpPage = new Page<>(
	// 		Convert.toLong(ServletUtils.getParameterToInt(MybatisPageConstants.PAGE_NUM), 1L)
	// 		, Convert.toLong(ServletUtils.getParameterToInt(MybatisPageConstants.PAGE_SIZE), 10L));
	// 	if (!CollectionUtil.isEmpty(sortingFields)) {
	// 		mpPage.addOrder(sortingFields.stream()
	// 			.map(sortingField -> SortingField.ORDER_ASC.equals(sortingField.getOrder()) ?
	// 				OrderItem.asc(sortingField.getField())
	// 				: OrderItem.desc(sortingField.getField()))
	// 			.collect(Collectors.toList()));
	// 	}
	// 	return selectPage(mpPage, queryWrapper);
	// }

	/**
	 * 选择一个
	 *
	 * @param field 场
	 * @param value 价值
	 * @return {@link T }
	 * @since 2022-09-07 08:52:08
	 */
	default T selectOne(String field, Object value) {
		return Optional.ofNullable(selectOne(new QueryWrapper<T>().eq(field, value)))
			.orElseThrow(BusinessException::notFoundException);
	}

	/**
	 * 选择一个
	 *
	 * @param field 场
	 * @param value 价值
	 * @return {@link T }
	 * @since 2022-09-07 08:52:08
	 */
	default T selectOne(SFunction<T, ?> field, Object value) {
		return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
	}

	/**
	 * 选择一个
	 *
	 * @param field1 field1
	 * @param value1 value1
	 * @param field2 field2
	 * @param value2 value2
	 * @return {@link T }
	 * @since 2022-09-07 08:52:08
	 */
	default T selectOne(String field1, Object value1, String field2, Object value2) {
		return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
	}

	/**
	 * 选择一个
	 *
	 * @param field1 field1
	 * @param value1 value1
	 * @param field2 field2
	 * @param value2 value2
	 * @return {@link T }
	 * @since 2022-09-07 08:52:08
	 */
	default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2,
		Object value2) {
		return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
	}

	/**
	 * 选择数
	 *
	 * @return {@link Long }
	 * @since 2022-09-07 08:52:09
	 */
	default Long selectCount() {
		return selectCount(new QueryWrapper<T>());
	}

	/**
	 * 选择数
	 *
	 * @param field 场
	 * @param value 价值
	 * @return {@link Long }
	 * @since 2022-09-07 08:52:09
	 */
	default Long selectCount(String field, Object value) {
		return selectCount(new QueryWrapper<T>().eq(field, value));
	}

	/**
	 * 选择数
	 *
	 * @param field 场
	 * @param value 价值
	 * @return {@link Long }
	 * @since 2022-09-07 08:52:09
	 */
	default Long selectCount(SFunction<T, ?> field, Object value) {
		return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
	}

	/**
	 * 选择列表
	 *
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-07 08:52:09
	 */
	default List<T> selectList() {
		return selectList(new QueryWrapper<>());
	}

	/**
	 * 选择列表
	 *
	 * @param field 场
	 * @param value 价值
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-07 08:52:09
	 */
	default List<T> selectList(String field, Object value) {
		return selectList(new QueryWrapper<T>().eq(field, value));
	}

	/**
	 * 选择列表
	 *
	 * @param field 场
	 * @param value 价值
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-07 08:52:09
	 */
	default List<T> selectList(SFunction<T, ?> field, Object value) {
		return selectList(new LambdaQueryWrapper<T>().eq(field, value));
	}

	/**
	 * 选择列表
	 *
	 * @param field  场
	 * @param values 值
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-07 08:52:09
	 */
	default List<T> selectList(String field, Collection<?> values) {
		return selectList(new QueryWrapper<T>().in(field, values));
	}

	/**
	 * 选择列表
	 *
	 * @param field  场
	 * @param values 值
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-07 08:52:09
	 */
	default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
		return selectList(new LambdaQueryWrapper<T>().in(field, values));
	}

	/**
	 * 插入批
	 *
	 * @param entities 实体
	 * @since 2022-09-07 08:52:09
	 */
	default void insertBatch(Collection<T> entities) {
		entities.forEach(this::insert);
	}

	/**
	 * 批处理更新
	 *
	 * @param update 更新
	 * @since 2022-09-07 08:52:09
	 */
	default void updateBatch(T update) {
		update(update, new QueryWrapper<>());
	}

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
	Integer insertBatchSomeColumn(Collection<T> entityList);

	/**
	 * 自定义批量插入 如果要自动填充，@Param(xx) xx参数名必须是 list/collection/array 3个的其中之一
	 */
	int insertBatch(@Param("list") List<T> list);

	/**
	 * 自定义批量更新，条件为主键 如果要自动填充，@Param(xx) xx参数名必须是 list/collection/array 3个的其中之一
	 */
	//int updateBatch(@Param("list") List<T> list);

	int batchSize = 1000;  // 应为mysql对于太长的sql语句是有限制的，所以我这里设置每1000条批量插入拼接sql

	default Integer batchInsert(Collection<T> entityList) {
		int result = 0;
		Collection<T> tempEntityList = new ArrayList<>();
		int i = 0;
		for (T entity : entityList) {
			tempEntityList.add(entity);
			if (i > 0 && (i % batchSize == 0)) {
				result += insertBatchSomeColumn(tempEntityList);
				tempEntityList.clear();
			}
			i++;
		}

		if (CollectionUtils.isNotEmpty(tempEntityList)) {
			result += insertBatchSomeColumn(tempEntityList);
		}
		return result;
	}


}
