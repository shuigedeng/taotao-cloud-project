package com.taotao.cloud.data.mongodb.helper.utils;

import com.taotao.cloud.data.mongodb.helper.reflection.ReflectionUtil;
import com.taotao.cloud.data.mongodb.helper.reflection.SerializableFunction;

import java.util.Collection;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * 查询语句生成器 OR连接
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-27 21:53:15
 */
public class CriteriaOrWrapper extends CriteriaWrapper {

	/**
	 * 标准或包装
	 *
	 * @since 2022-05-27 21:53:16
	 */
	public CriteriaOrWrapper() {
		andLink = false;
	}

	/**
	 * 或
	 *
	 * @param criteria 标准
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	public CriteriaOrWrapper or(Criteria criteria) {
		list.add(criteria);
		return this;
	}

	/**
	 * 或
	 *
	 * @param criteriaWrapper 标准包装
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	public CriteriaOrWrapper or(CriteriaWrapper criteriaWrapper) {
		list.add(criteriaWrapper.build());
		return this;
	}

//	/**
//	 * 等于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaWrapper
//	 */
//	public CriteriaOrWrapper eq(String column, Object params) {
//		super.eq(column, params);
//		return this;
//	}

	/**
	 * 等于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper eq(SerializableFunction<E, R> column, Object params) {
		super.eq(column, params);
		return this;
	}

//	/**
//	 * 不等于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper ne(String column, Object params) {
//		super.ne(column, params);
//		return this;
//	}

	/**
	 * 不等于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper ne(SerializableFunction<E, R> column, Object params) {
		super.ne(column, params);
		return this;
	}

//	/**
//	 * 小于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper lt(String column, Object params) {
//		super.lt(column, params);
//		return this;
//	}

	/**
	 * 小于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper lt(SerializableFunction<E, R> column, Object params) {
		super.lt(column, params);
		return this;
	}

//	/**
//	 * 小于或等于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper lte(String column, Object params) {
//		super.lte(column, params);
//		return this;
//	}

	/**
	 * 小于或等于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper lte(SerializableFunction<E, R> column, Object params) {
		super.lte(column, params);
		return this;
	}

//	/**
//	 * 大于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper gt(String column, Object params) {
//		super.gt(column, params);
//		return this;
//	}

	/**
	 * 大于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper gt(SerializableFunction<E, R> column, Object params) {
		super.gt(column, params);
		return this;
	}

//	/**
//	 * 大于或等于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper gte(String column, Object params) {
//		super.gte(column, params);
//		return this;
//	}

	/**
	 * 大于或等于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper gte(SerializableFunction<E, R> column, Object params) {
		super.gte(column, params);
		return this;
	}

//	/**
//	 * 包含
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper contain(String column, Object params) {
//		super.contain(column, params);
//		return this;
//	}

	/**
	 * 包含
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper contain(SerializableFunction<E, R> column, Object params) {
		super.contain(column, params);
		return this;
	}

//	/**
//	 * 包含,以或连接
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper containOr(String column, Collection<?> params) {
//		super.containOr(column, params);
//		return this;
//	}

	/**
	 * 包含,以或连接
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper containOr(SerializableFunction<E, R> column,
		Collection<?> params) {
		super.containOr(column, params);
		return this;
	}

//	/**
//	 * 包含,以或连接
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper containOr(String column, Object[] params) {
//		super.containOr(column, params);
//		return this;
//	}

	/**
	 * 包含,以或连接
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper containOr(SerializableFunction<E, R> column, Object[] params) {
		super.containOr(column, params);
		return this;
	}

//	/**
//	 * 包含,以且连接
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper containAnd(String column, Collection<?> params) {
//		super.containAnd(column, params);
//		return this;
//	}

	/**
	 * 包含,以且连接
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper containAnd(SerializableFunction<E, R> column,
		Collection<?> params) {
		super.containAnd(column, params);
		return this;
	}

//	/**
//	 * 包含,以且连接
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper containAnd(String column, Object[] params) {
//		super.containAnd(column, params);
//		return this;
//	}

	/**
	 * 包含,以且连接
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper containAnd(SerializableFunction<E, R> column, Object[] params) {
		super.containAnd(column, params);
		return this;
	}

//	/**
//	 * 相似于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper like(String column, String params) {
//		super.like(column, params);
//		return this;
//	}

	/**
	 * 相似于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper like(SerializableFunction<E, R> column, String params) {
		super.like(column, params);
		return this;
	}

//	/**
//	 * 在其中
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper in(String column, Collection<?> params) {
//		super.in(column, params);
//		return this;
//	}

	/**
	 * 在其中
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	public <E, R> CriteriaOrWrapper in(SerializableFunction<E, R> column, Collection<?> params) {
		super.in(column, params);
		return this;
	}
//
//	/**
//	 * 在其中
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper in(String column, Object[] params) {
//		super.in(column, params);
//		return this;
//	}

	/**
	 * 在其中
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper in(SerializableFunction<E, R> column, Object[] params) {
		super.in(column, params);
		return this;
	}

//	/**
//	 * 不在其中
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper nin(String column, Collection<?> params) {
//		super.nin(column, params);
//		return this;
//	}

	/**
	 * 不在其中
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	public <E, R> CriteriaOrWrapper nin(SerializableFunction<E, R> column, Collection<?> params) {
		super.nin(column, params);
		return this;
	}

//	/**
//	 * 不在其中
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper nin(String column, Object[] params) {
//		super.nin(column, params);
//		return this;
//	}

	/**
	 * 不在其中
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper nin(SerializableFunction<E, R> column, Object[] params) {
		super.nin(column, params);
		return this;
	}

//	/**
//	 * 为空
//	 * 
//	 *
//	 * @param column 字段
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper isNull(String column) {
//		super.isNull(column);
//		return this;
//	}

	/**
	 * 为空
	 *
	 * @param column 字段
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper isNull(SerializableFunction<E, R> column) {
		super.isNull(column);
		return this;
	}

//	/**
//	 * 不为空
//	 * 
//	 *
//	 * @param column 字段
//	 * @return CriteriaOrWrapper
//	 */
//	public CriteriaOrWrapper isNotNull(String column) {
//		super.isNotNull(column);
//		return this;
//	}

	/**
	 * 不为空
	 *
	 * @param column 字段
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	@Override
	public <E, R> CriteriaOrWrapper isNotNull(SerializableFunction<E, R> column) {
		super.isNotNull(column);
		return this;
	}
//
//	/**
//	 * 数组查询
//	 * 
//	 * @param arr    数组名
//	 * @param column 字段名
//	 * @param param  字段值
//	 * @return
//	 */
//	public CriteriaOrWrapper findArray(String arr, String column, String param) {
//		super.findArray(arr, column, param);
//		return this;
//	}

	/**
	 * 数组查询
	 *
	 * @param arr    数组名
	 * @param column 字段名
	 * @param param  字段值
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	public <E, R> CriteriaOrWrapper findArray(SerializableFunction<E, R> arr,
		SerializableFunction<E, R> column, String param) {
		super.findArray(ReflectionUtil.getFieldName(arr), column, param);
		return this;
	}

//	/**
//	 * 数组模糊查询
//	 * 
//	 * @param arr    数组名
//	 * @param column 字段名
//	 * @param param  字段值
//	 * @return
//	 */
//	public CriteriaOrWrapper findArrayLike(String arr, String column, String param) {
//		super.findArrayLike(arr, column, param);
//		return this;
//	}

	/**
	 * 数组模糊查询
	 *
	 * @param arr    数组名
	 * @param column 字段名
	 * @param param  字段值
	 * @return {@link CriteriaOrWrapper }
	 * @since 2022-05-27 21:53:16
	 */
	public <E, R> CriteriaOrWrapper findArrayLike(SerializableFunction<E, R> arr,
		SerializableFunction<E, R> column, String param) {
		super.findArrayLike(ReflectionUtil.getFieldName(arr), column, param);
		return this;
	}
}
