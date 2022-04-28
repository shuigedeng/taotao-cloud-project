package com.taotao.cloud.common.support.condition;

/**
 * 条件接口
 * 迁移到
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:07:57
 */
public interface ICondition<T> {

	/**
	 * 满足条件
	 *
	 * @param t 元素
	 * @return boolean
	 * @since 2022-04-27 17:07:57
	 */
	boolean condition(final T t);

}
