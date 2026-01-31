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

package com.taotao.cloud.ccsr.core.serializer;

import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SerializeFactory
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class SerializeFactory {

    public static final String HESSIAN_INDEX = "Hessian".toLowerCase();

    private static final Map<String, Serializer> SERIALIZER_MAP = new HashMap<>(4);

    public static String DEFAULT_SERIALIZER = HESSIAN_INDEX;

    static {
        List<Serializer> extensions = SpiExtensionFactory.getExtensions(Serializer.class);
        extensions.forEach(item -> SERIALIZER_MAP.put(item.name().toLowerCase(), item));
    }

    public static Serializer getDefault() {
        return SERIALIZER_MAP.get(DEFAULT_SERIALIZER);
    }

    public static Serializer getSerializer( String type ) {
        return SERIALIZER_MAP.get(type.toLowerCase());
    }
}
