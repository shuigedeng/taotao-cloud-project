package com.taotao.cloud.core.heaven.support.proxy;

/**
 * 代理接口
 */
public interface IProxy {

    /**
     * 获取代理对象
     * 1. 如果是实现了接口，默认使用 dynamic proxy 即可。
     * 2. 如果没有实现接口，默认使用 CGLIB 实现代理。
     * @return 代理对象
     */
    Object proxy();

}
