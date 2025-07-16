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

package com.taotao.cloud.auth.biz.jpa.converter;

import com.taotao.cloud.auth.biz.jpa.entity.RegisteredClientDetails;
import com.taotao.cloud.auth.biz.jpa.jackson2.OAuth2JacksonProcessor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * <p>RegisteredClient 转换器</p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:09:25
 */
public abstract class AbstractRegisteredClientConverter<S extends RegisteredClientDetails>
        extends AbstractOAuth2EntityConverter<S, RegisteredClient>
        implements RegisteredClientConverter<S> {

    public AbstractRegisteredClientConverter(OAuth2JacksonProcessor jacksonProcessor) {
        super(jacksonProcessor);
    }

    @Override
    public RegisteredClient convert(S details) {
        return RegisteredClientConverter.super.convert(details);
    }
}
