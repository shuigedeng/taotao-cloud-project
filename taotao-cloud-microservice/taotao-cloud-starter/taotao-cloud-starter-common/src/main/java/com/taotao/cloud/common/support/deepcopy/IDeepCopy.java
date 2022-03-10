
package com.taotao.cloud.common.support.deepcopy;

/**
 * 深度拷贝接口定义
 */
public interface IDeepCopy {

    /**
     * 深度拷贝
     * @param object 原始对象
     * @return 结果
     * @since 0.0.1
     * @param <T> 泛型
     */
    <T> T deepCopy(T object);

}
