package com.taotao.cloud.common.support.filter;

/**
 * 过滤接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:08:31
 */
public interface IFilter<T> {

	/**
	 * 过滤
	 *
	 * @param t 元素
	 * @return 结果
	 */
	boolean filter(final T t);

}
