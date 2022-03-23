package com.taotao.cloud.common.support.filler;

/**
 * 将 T 信息进行填充，并且返回填充后的对象
 */
public interface IFiller<T> {

	/**
	 * 填充原始对象 1. 为了实现简单，不做任何返回值。
	 *
	 * @param t 原始对象
	 */
	void fill(T t);

}
