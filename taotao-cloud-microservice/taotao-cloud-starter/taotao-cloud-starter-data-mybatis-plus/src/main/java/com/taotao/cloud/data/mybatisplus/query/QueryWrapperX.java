package com.taotao.cloud.data.mybatisplus.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import java.util.Collection;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-07 08:52:38
 */
public class QueryWrapperX<T> extends QueryWrapper<T> {

	/**
	 * 如果现在
	 *
	 * @param column 列
	 * @param val    瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> likeIfPresent(String column, String val) {
		if (StringUtils.hasText(val)) {
			return (QueryWrapperX<T>) super.like(column, val);
		}
		return this;
	}

	/**
	 * 如果现在
	 *
	 * @param column 列
	 * @param values 值
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> inIfPresent(String column, Collection<?> values) {
		if (!CollectionUtils.isEmpty(values)) {
			return (QueryWrapperX<T>) super.in(column, values);
		}
		return this;
	}

	/**
	 * 如果现在
	 *
	 * @param column 列
	 * @param values 值
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> inIfPresent(String column, Object... values) {
		if (!ArrayUtils.isEmpty(values)) {
			return (QueryWrapperX<T>) super.in(column, values);
		}
		return this;
	}

	/**
	 * 情商如果存在
	 *
	 * @param column 列
	 * @param val    瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> eqIfPresent(String column, Object val) {
		if (!StringUtils.isEmpty(val)) {
			return (QueryWrapperX<T>) super.eq(column, val);
		}
		return this;
	}

	/**
	 * 不如果存在
	 *
	 * @param column 列
	 * @param val    瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> neIfPresent(String column, Object val) {
		if (!StringUtils.isEmpty(val)) {
			return (QueryWrapperX<T>) super.ne(column, val);
		}
		return this;
	}

	/**
	 * gt如果存在
	 *
	 * @param column 列
	 * @param val    瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> gtIfPresent(String column, Object val) {
		if (!StringUtils.isEmpty(val)) {
			return (QueryWrapperX<T>) super.gt(column, val);
		}
		return this;
	}

	/**
	 * 通用电气如果存在
	 *
	 * @param column 列
	 * @param val    瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> geIfPresent(String column, Object val) {
		if (!StringUtils.isEmpty(val)) {
			return (QueryWrapperX<T>) super.ge(column, val);
		}
		return this;
	}

	/**
	 * lt如果存在
	 *
	 * @param column 列
	 * @param val    瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> ltIfPresent(String column, Object val) {
		if (!StringUtils.isEmpty(val)) {
			return (QueryWrapperX<T>) super.lt(column, val);
		}
		return this;
	}

	/**
	 * le如果存在
	 *
	 * @param column 列
	 * @param val    瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> leIfPresent(String column, Object val) {
		if (!StringUtils.isEmpty(val)) {
			return (QueryWrapperX<T>) super.le(column, val);
		}
		return this;
	}

	/**
	 * 之间如果存在
	 *
	 * @param column 列
	 * @param val1   val1
	 * @param val2   val2
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> betweenIfPresent(String column, Object val1, Object val2) {
		if (!StringUtils.isEmpty(val1) && !StringUtils.isEmpty(val2)) {
			return (QueryWrapperX<T>) super.between(column, val1, val2);
		}
		if (!StringUtils.isEmpty(val1)) {
			return (QueryWrapperX<T>) ge(column, val1);
		}
		if (!StringUtils.isEmpty(val2)) {
			return (QueryWrapperX<T>) le(column, val2);
		}
		return this;
	}

	/**
	 * 应用程序如果存在
	 *
	 * @param applySql 应用sql
	 * @param values   值
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	public QueryWrapperX<T> appIfPresent(String applySql, Object values) {
		if (!StringUtils.isEmpty(values)) {
			return (QueryWrapperX<T>) super.apply(applySql, values);
		}
		return this;
	}

	// ========== 重写父类方法，方便链式调用 ==========

	/**
	 * 情商
	 *
	 * @param condition 条件
	 * @param column    列
	 * @param val       瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	@Override
	public QueryWrapperX<T> eq(boolean condition, String column, Object val) {
		super.eq(condition, column, val);
		return this;
	}

	/**
	 * 情商
	 *
	 * @param column 列
	 * @param val    瓦尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	@Override
	public QueryWrapperX<T> eq(String column, Object val) {
		super.eq(column, val);
		return this;
	}

	/**
	 * order by desc
	 *
	 * @param column 列
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	@Override
	public QueryWrapperX<T> orderByDesc(String column) {
		super.orderByDesc(true, column);
		return this;
	}

	/**
	 * 最后
	 *
	 * @param lastSql 最后一个sql
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	@Override
	public QueryWrapperX<T> last(String lastSql) {
		super.last(lastSql);
		return this;
	}

	/**
	 * 在
	 *
	 * @param column 列
	 * @param coll   科尔
	 * @return {@link QueryWrapperX }<{@link T }>
	 * @since 2022-09-07 08:52:38
	 */
	@Override
	public QueryWrapperX<T> in(String column, Collection<?> coll) {
		super.in(column, coll);
		return this;
	}

}
