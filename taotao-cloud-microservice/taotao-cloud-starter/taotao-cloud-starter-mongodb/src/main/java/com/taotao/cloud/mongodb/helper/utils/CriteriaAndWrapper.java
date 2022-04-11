package com.taotao.cloud.mongodb.helper.utils;

import com.taotao.cloud.mongodb.helper.reflection.ReflectionUtil;
import com.taotao.cloud.mongodb.helper.reflection.SerializableFunction;
import java.util.Collection;
import org.springframework.data.mongodb.core.query.Criteria;


/**
 * 查询语句生成器 AND连接
 */
public class CriteriaAndWrapper extends CriteriaWrapper {

	public CriteriaAndWrapper() {
		andLink = true;
	}

	public CriteriaAndWrapper and(Criteria criteria) {
		list.add(criteria);
		return this;
	}

	public CriteriaAndWrapper and(CriteriaWrapper criteriaWrapper) {
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
//	public CriteriaAndWrapper eq(String column, Object params) {
//		super.eq(column, params);
//		return this;
//	}

	/**
	 * 等于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper eq(SerializableFunction<E, R> column, Object params) {
		super.eq(column, params);
		return this;
	}

//	/**
//	 * 不等于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper ne(String column, Object params) {
//		super.ne(column, params);
//		return this;
//	}

	/**
	 * 不等于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper ne(SerializableFunction<E, R> column, Object params) {
		super.ne(column, params);
		return this;
	}

//	/**
//	 * 小于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper lt(String column, Object params) {
//		super.lt(column, params);
//		return this;
//	}

	/**
	 * 小于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper lt(SerializableFunction<E, R> column, Object params) {
		super.lt(column, params);
		return this;
	}

//	/**
//	 * 小于或等于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper lte(String column, Object params) {
//		super.lte(column, params);
//		return this;
//	}

	/**
	 * 小于或等于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper lte(SerializableFunction<E, R> column, Object params) {
		super.lte(column, params);
		return this;
	}

//	/**
//	 * 大于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper gt(String column, Object params) {
//		super.gt(column, params);
//		return this;
//	}

	/**
	 * 大于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper gt(SerializableFunction<E, R> column, Object params) {
		super.gt(column, params);
		return this;
	}

//	/**
//	 * 大于或等于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper gte(String column, Object params) {
//		super.gte(column, params);
//		return this;
//	}

	/**
	 * 大于或等于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper gte(SerializableFunction<E, R> column, Object params) {
		super.gte(column, params);
		return this;
	}

//	/**
//	 * 包含
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper contain(String column, Object params) {
//		super.contain(column, params);
//		return this;
//	}

	/**
	 * 包含
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper contain(SerializableFunction<E, R> column, Object params) {
		super.contain(column, params);
		return this;
	}

//	/**
//	 * 包含,以或连接
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper containOr(String column, Collection<?> params) {
//		super.containOr(column, params);
//		return this;
//	}

	/**
	 * 包含,以或连接
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	public <E, R> CriteriaAndWrapper containOr(SerializableFunction<E, R> column,
		Collection<?> params) {
		super.containOr(column, params);
		return this;
	}

//	/**
//	 * 包含,以或连接
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper containOr(String column, Object[] params) {
//		super.containOr(column, params);
//		return this;
//	}

	/**
	 * 包含,以或连接
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper containOr(SerializableFunction<E, R> column, Object[] params) {
		super.containOr(column, params);
		return this;
	}

//	/**
//	 * 包含,以且连接
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper containAnd(String column, Collection<?> params) {
//		super.containAnd(column, params);
//		return this;
//	}

	/**
	 * 包含,以且连接
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper containAnd(SerializableFunction<E, R> column,
		Collection<?> params) {
		super.containAnd(column, params);
		return this;
	}

//	/**
//	 * 包含,以且连接
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper containAnd(String column, Object[] params) {
//		super.containAnd(column, params);
//		return this;
//	}

	/**
	 * 包含,以且连接
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper containAnd(SerializableFunction<E, R> column,
		Object[] params) {
		super.containAnd(column, params);
		return this;
	}

//	/**
//	 * 相似于
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper like(String column, String params) {
//		super.like(column, params);
//		return this;
//	}

	/**
	 * 相似于
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper like(SerializableFunction<E, R> column, String params) {
		super.like(column, params);
		return this;
	}

//	/**
//	 * 在其中
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper in(String column, Collection<?> params) {
//		super.in(column, params);
//		return this;
//	}

	/**
	 * 在其中
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper in(SerializableFunction<E, R> column, Collection<?> params) {
		super.in(column, params);
		return this;
	}

//	/**
//	 * 在其中
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper in(String column, Object[] params) {
//		super.in(column, params);
//		return this;
//	}

	/**
	 * 在其中
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper in(SerializableFunction<E, R> column, Object[] params) {
		super.in(column, params);
		return this;
	}

//	/**
//	 * 不在其中
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper nin(String column, Collection<?> params) {
//		super.nin(column, params);
//		return this;
//	}

	/**
	 * 不在其中
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper nin(SerializableFunction<E, R> column, Collection<?> params) {
		super.nin(column, params);
		return this;
	}

//	/**
//	 * 不在其中
//	 * 
//	 * @param column 字段
//	 * @param params 参数
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper nin(String column, Object[] params) {
//		super.nin(column, params);
//		return this;
//	}

	/**
	 * 不在其中
	 *
	 * @param column 字段
	 * @param params 参数
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper nin(SerializableFunction<E, R> column, Object[] params) {
		super.nin(column, params);
		return this;
	}

//	/**
//	 * 为空
//	 * 
//	 *
//	 * @param column 字段
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper isNull(String column) {
//		super.isNull(column);
//		return this;
//	}

	/**
	 * 为空
	 *
	 * @param column 字段
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper isNull(SerializableFunction<E, R> column) {
		super.isNull(column);
		return this;
	}

//	/**
//	 * 不为空
//	 * 
//	 *
//	 * @param column 字段
//	 * @return CriteriaAndWrapper
//	 */
//	public CriteriaAndWrapper isNotNull(String column) {
//		super.isNotNull(column);
//		return this;
//	}

	/**
	 * 不为空
	 *
	 * @param column 字段
	 * @return CriteriaAndWrapper
	 */
	@Override
	public <E, R> CriteriaAndWrapper isNotNull(SerializableFunction<E, R> column) {
		super.isNotNull(column);
		return this;
	}

//	/**
//	 * 数组查询
//	 * 
//	 * @param arr    数组名
//	 * @param column 字段名
//	 * @param param  字段值
//	 * @return
//	 */
//	public CriteriaAndWrapper findArray(String arr, String column, String param) {
//		super.findArray(arr, column, param);
//		return this;
//	}

	/**
	 * 数组查询
	 *
	 * @param arr    数组名
	 * @param column 字段名
	 * @param param  字段值
	 */
	public <E, R> CriteriaAndWrapper findArray(SerializableFunction<E, R> arr,
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
//	public CriteriaAndWrapper findArrayLike(String arr, String column, String param) {
//		super.findArrayLike(arr, column, param);
//		return this;
//	}

	/**
	 * 数组模糊查询
	 *
	 * @param arr    数组名
	 * @param column 字段名
	 * @param param  字段值
	 */
	public <E, R> CriteriaAndWrapper findArrayLike(SerializableFunction<E, R> arr,
		SerializableFunction<E, R> column, String param) {
		super.findArrayLike(ReflectionUtil.getFieldName(arr), column, param);
		return this;
	}
}
