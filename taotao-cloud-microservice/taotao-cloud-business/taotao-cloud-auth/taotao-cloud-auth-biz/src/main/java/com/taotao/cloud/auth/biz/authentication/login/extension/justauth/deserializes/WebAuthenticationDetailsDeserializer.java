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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.deserializes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * WebAuthenticationDetails Jackson 反序列化
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/28 17:19
 */
public class WebAuthenticationDetailsDeserializer extends StdDeserializer<WebAuthenticationDetails> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public WebAuthenticationDetailsDeserializer() {
        super(WebAuthenticationDetails.class);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public WebAuthenticationDetails deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode jsonNode = mapper.readTree(p);

        final Class<WebAuthenticationDetails> detailsClass = WebAuthenticationDetails.class;
        try {
            final Class<String> stringClass = String.class;
            final Constructor<WebAuthenticationDetails> privateConstructor =
                    detailsClass.getDeclaredConstructor(stringClass, stringClass);
            privateConstructor.setAccessible(true);
            final String remoteAddress = jsonNode.get("remoteAddress").asText(null);
            final String sessionId = jsonNode.get("sessionId").asText(null);
            return privateConstructor.newInstance(remoteAddress, sessionId);
        } catch (NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            final String msg = String.format("WebAuthenticationDetails Jackson 反序列化错误: %s", e.getMessage());
            log.error(msg);
            throw new IOException(msg, e);
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonDeserialize(using = WebAuthenticationDetailsDeserializer.class)
    public interface WebAuthenticationDetailsMixin {}
}
