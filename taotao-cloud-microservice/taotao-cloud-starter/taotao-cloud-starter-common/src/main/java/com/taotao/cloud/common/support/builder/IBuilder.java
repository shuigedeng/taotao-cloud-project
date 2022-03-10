package com.taotao.cloud.common.support.builder;

/**
 * 构建者模式接口
 */
public interface IBuilder<T> {

	/**
	 * 构建
	 *
	 * @return 返回的对象
	 */
	T build();

}
