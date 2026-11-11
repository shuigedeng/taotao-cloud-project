package com.taotao.cloud.rpc.common.spi;

import com.taotao.cloud.rpc.common.spi.AdaptiveExtensionFactory;

/**
 * 扩展对象工厂
 * <p>
 * 用于在 SPI 注入场景下创建扩展对象（如 Spring Bean 注入）。
 * 默认实现为 {@link AdaptiveExtensionFactory}，组合多种工厂。
 */
public interface ExtensionFactory {

    /**
     * 获取扩展对象
     *
     * @param type 对象类型
     * @param name 名称
     * @return 对象实例
     */
    <T> T getExtension(Class<?> type, String name);
}
