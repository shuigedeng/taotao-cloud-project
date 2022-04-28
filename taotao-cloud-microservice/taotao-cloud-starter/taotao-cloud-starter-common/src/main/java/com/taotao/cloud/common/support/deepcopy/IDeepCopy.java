
package com.taotao.cloud.common.support.deepcopy;

/**
 * 深度拷贝接口定义
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:08:07
 */
public interface IDeepCopy {

	/**
	 * 深度拷贝
	 *
	 * @param object 原始对象
	 * @param <T>    泛型
	 * @return 结果
	 */
	<T> T deepCopy(T object);

}
