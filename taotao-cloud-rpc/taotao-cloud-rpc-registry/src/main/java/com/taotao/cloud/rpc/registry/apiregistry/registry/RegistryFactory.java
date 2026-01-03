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

package com.taotao.cloud.rpc.registry.apiregistry.registry;

import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;

/**
 * RegistryFactory
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class RegistryFactory {

    public static BaseRegistry create() {
        //		String type = ApiRegistryProperties.getRegistryType();
        String type = "";
        if (RedisRegistry.class.getSimpleName().equalsIgnoreCase(type)) {
            return new RedisRegistry();
        }
        if (NacosRegistry.class.getSimpleName().equalsIgnoreCase(type)) {
            return new NacosRegistry();
        }
        if (NoneRegistry.class.getSimpleName().equalsIgnoreCase(type)) {
            return new NoneRegistry();
        }
        throw new ApiRegistryException("请配置ttc.apiRegistry.registry.type");
    }
}
