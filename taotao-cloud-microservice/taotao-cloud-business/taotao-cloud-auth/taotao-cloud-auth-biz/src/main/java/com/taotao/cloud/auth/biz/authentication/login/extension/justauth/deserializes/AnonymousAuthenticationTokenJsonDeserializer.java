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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * AnonymousAuthenticationToken Jackson 反序列化
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/28 10:58
 */
public class AnonymousAuthenticationTokenJsonDeserializer extends StdDeserializer<AnonymousAuthenticationToken> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public AnonymousAuthenticationTokenJsonDeserializer() {
        super(AnonymousAuthenticationToken.class);
    }

    @Override
    public AnonymousAuthenticationToken deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        final JsonNode jsonNode = mapper.readTree(p);

        // 获取 authorities
        Collection<? extends GrantedAuthority> authorities =
                mapper.convertValue(jsonNode.get("authorities"),
                                    new TypeReference<Collection<SimpleGrantedAuthority>>() {});

        final JsonNode detailsNode = jsonNode.get("details");

        final Integer key = jsonNode.get("keyHash").asInt();
        final String principal = jsonNode.get("principal").asText("anonymousUser");

        // 创建 AnonymousAuthenticationToken 对象
        AnonymousAuthenticationToken token;
        try {
            final Constructor<AnonymousAuthenticationToken> declaredConstructor =
                    AnonymousAuthenticationToken.class.getDeclaredConstructor(Integer.class, Object.class, Collection.class);
            declaredConstructor.setAccessible(true);
            token = declaredConstructor.newInstance(key, principal, authorities);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            final String msg = String.format("AnonymousAuthenticationToken Jackson 反序列化错误: principal 反序列化错误: %s",
                                             e.getMessage());
            log.error(msg);
            throw new IOException(msg);
        }

        // 创建 details 对象
        WebAuthenticationDetails details = mapper.convertValue(detailsNode, new TypeReference<WebAuthenticationDetails>(){});
        token.setDetails(details);

        return token;

    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonDeserialize(using = AnonymousAuthenticationTokenJsonDeserializer.class)
    public interface AnonymousAuthenticationTokenMixin {}

}
