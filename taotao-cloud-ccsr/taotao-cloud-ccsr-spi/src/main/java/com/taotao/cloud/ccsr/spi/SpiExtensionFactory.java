/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.ccsr.spi;

import java.util.List;
import java.util.Optional;

/**
 * SpiExtensionFactory
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class SpiExtensionFactory {

    public static <T> T getDefaultExtension( final Class<T> clazz ) {
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

    public static <T> T getExtension( String key, final Class<T> clazz ) {
        return ExtensionLoader.getExtensionLoader(clazz).getJoin(key);
    }

    public static <T> List<T> getExtensions( final Class<T> clazz ) {
        return ExtensionLoader.getExtensionLoader(clazz).getJoins();
    }
}
