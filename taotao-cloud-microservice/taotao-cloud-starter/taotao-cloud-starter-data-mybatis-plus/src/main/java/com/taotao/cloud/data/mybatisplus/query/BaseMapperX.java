package com.taotao.cloud.data.mybatisplus.query;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.taotao.cloud.common.model.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 在 MyBatis Plus 的 BaseMapper 的基础上拓展，提供更多的能力
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-07 08:52:08
 */
public interface BaseMapperX<T> extends BaseMapper<T> {

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
		return selectOne(new QueryWrapper<T>().eq(field, value));
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
}
