package com.taotao.cloud.rpc.client.client.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * 参考：https://blog.csdn.net/u012240455/article/details/79210250
 *
 * （1）方法执行并不需要一定要有实现类。
 * （2）直接根据反射即可处理相关信息。
 * （3）rpc 是一种强制根据接口进行编程的实现方式。
 * @author shuigedeng
 * @since 2024.06
 * @param <T> 泛型
 */
public interface ReferenceProxy<T> extends InvocationHandler {

    /**
     * 获取代理实例
     * （1）接口只是为了代理。
     * （2）实际调用中更加关心 的是 serviceId
     * @return 代理实例
     * @since 2024.06
     */
    @SuppressWarnings("unchecked")
    T proxy();

}
