package com.taotao.cloud.pay;

/**
 * 用于构建对象的接口
 *
 * @param <O> 正在构建的对象的类型
 */
public interface PayBuilder<O> {


	/**
	 * 构建对象并返回它或null。
	 *
	 * @return 如果实现允许，则要构建的对象或null。
	 */
	O build();
}
