package com.taotao.cloud.common.support.builder;

/**
 * 构建者模式接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:07:16
 */
public interface IBuilder<T> {

	/**
	 * 构建
	 *
	 * @return {@link T }
	 * @since 2022-04-27 17:07:16
	 */
	T build();

}
