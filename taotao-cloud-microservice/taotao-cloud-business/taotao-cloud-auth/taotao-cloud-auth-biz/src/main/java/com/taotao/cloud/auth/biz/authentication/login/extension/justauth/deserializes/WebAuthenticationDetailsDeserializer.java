/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            final String msg = String.format("WebAuthenticationDetails Jackson 反序列化错误: %s", e.getMessage());
            log.error(msg);
            throw new IOException(msg, e);
        }

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonDeserialize(using = WebAuthenticationDetailsDeserializer.class)
    public interface WebAuthenticationDetailsMixin {}

}
