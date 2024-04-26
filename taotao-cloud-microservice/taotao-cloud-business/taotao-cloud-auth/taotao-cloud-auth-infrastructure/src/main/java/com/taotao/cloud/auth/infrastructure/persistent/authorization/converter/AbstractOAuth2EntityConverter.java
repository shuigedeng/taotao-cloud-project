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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.converter;

import com.taotao.cloud.auth.infrastructure.persistent.authorization.jackson2.OAuth2JacksonProcessor;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;

/**
 * <p>封装RegisteredClientAdapter 默认行为 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:09:30
 */
public abstract class AbstractOAuth2EntityConverter<S, T> implements Converter<S, T> {

    private final OAuth2JacksonProcessor jacksonProcessor;

    public AbstractOAuth2EntityConverter(OAuth2JacksonProcessor jacksonProcessor) {
        this.jacksonProcessor = jacksonProcessor;
    }

    protected Map<String, Object> parseMap(String data) {
        return jacksonProcessor.parseMap(data);
    }

    protected String writeMap(Map<String, Object> data) {
        return jacksonProcessor.writeMap(data);
    }
}
