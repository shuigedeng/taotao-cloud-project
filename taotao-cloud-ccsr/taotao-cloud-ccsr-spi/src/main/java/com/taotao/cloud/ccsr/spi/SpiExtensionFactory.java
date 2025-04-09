package com.taotao.cloud.ccsr.spi;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

public class SpiExtensionFactory {

    public static <T> T getDefaultExtension(final Class<T> clazz) {
        return Optional.ofNullable(clazz)
                // 入参clazz必须是接口
                .filter(Class::isInterface)
                // 入参clazz必须被@SPI标识
                .filter(cls -> cls.isAnnotationPresent(SPI.class))
                // 基于clazz这个接口类型实例化ExtensionLoader
                .map(ExtensionLoader::getExtensionLoader)
                // 获取该@SPI标识接口的默认实现，不存在则返回NULL
                .map(ExtensionLoader::getDefaultJoin)
                .orElse(null);
    }

    public static <T> T getExtension(String key, final Class<T> clazz) {
        return ExtensionLoader.getExtensionLoader(clazz).getJoin(key);
    }

    public static <T> List<T> getExtensions(final Class<T> clazz) {
        return ExtensionLoader.getExtensionLoader(clazz).getJoins();
    }
}
