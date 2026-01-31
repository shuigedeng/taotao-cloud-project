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

package com.taotao.cloud.ccsr.client.listener;

import com.taotao.cloud.ccsr.common.exception.CcsrClientException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * AbstractConfigListener
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public abstract class AbstractConfigListener<T extends ConfigData> implements ConfigListener<T> {

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    protected AbstractConfigListener() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            this.type = (Class<T>) ( (ParameterizedType) superClass ).getActualTypeArguments()[0];
        } else {
            throw new CcsrClientException("Unable to determine generic type for listener.");
        }
    }

    public Class<T> getType() {
        return this.type;
    }

    @Override
    public void register() {
        ConfigListenerManager.registerListener(getType(), this);
    }
}
