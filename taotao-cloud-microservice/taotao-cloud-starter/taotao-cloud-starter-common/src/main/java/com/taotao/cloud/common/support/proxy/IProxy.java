package com.taotao.cloud.common.support.proxy;

/**
 * 代理接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:11:04
 */
public interface IProxy {

	/**
	 * 获取代理对象 1. 如果是实现了接口，默认使用 dynamic proxy 即可。 2. 如果没有实现接口，默认使用 CGLIB 实现代理。
	 *
	 * @return 代理对象
	 */
	Object proxy();

}
