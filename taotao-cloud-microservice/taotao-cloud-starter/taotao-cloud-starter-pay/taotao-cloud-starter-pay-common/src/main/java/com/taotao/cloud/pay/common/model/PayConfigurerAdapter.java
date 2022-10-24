package com.taotao.cloud.pay.common.model;

/**
 * 支付配置适配，主要用于外部调用者链式的方式创建对象
 *
 * @param <B> 返回对应的服务构建器
 */
public interface PayConfigurerAdapter<B> {

	/**
	 * 外部调用者使用，链式的做法
	 *
	 * @return 返回对应外部调用者
	 */
	B and();

	/**
	 * 获取构建器
	 *
	 * @return 构建器
	 */
	B getBuilder();
}
