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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.JustAuthAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.util.Collection;

/**
 * Auth2AuthenticationToken Jackson 反序列化
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/28 10:58
 */
public class Auth2AuthenticationTokenJsonDeserializer extends StdDeserializer<JustAuthAuthenticationToken> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Auth2AuthenticationTokenJsonDeserializer() {
        super(JustAuthAuthenticationToken.class);
    }

    @Override
    public JustAuthAuthenticationToken deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        final JsonNode jsonNode = mapper.readTree(p);

        // 获取 authorities
        Collection<? extends GrantedAuthority> tokenAuthorities =
                mapper.convertValue(jsonNode.get("authorities"),
                                    new TypeReference<Collection<SimpleGrantedAuthority>>() {});

        final String providerId = jsonNode.get("providerId").asText(null);
        final JsonNode detailsNode = jsonNode.get("details");
        final JsonNode principalNode = jsonNode.get("principal");

        // 创建 principal 对象
        Object principal;
        // 获取 principal 实际的全类名
        final String principalString = principalNode.toString();
        String principalClassName = principalString.substring(1);
        if (principalClassName.startsWith("\"@class\":\"")) {
            principalClassName = principalClassName.substring(principalClassName.indexOf("\"@class\":\"") + 10);
        } else {
            principalClassName = principalClassName.substring(1);
        }
        principalClassName = principalClassName.substring(0, principalClassName.indexOf("\""));

        try {
            final Class<?> principalClass = Class.forName(principalClassName);
            final JavaType javaType = mapper.getTypeFactory().constructType(principalClass);
            principal = mapper.convertValue(principalNode, javaType);
        }
        catch (Exception e) {
            final String msg = String.format("Auth2AuthenticationToken Jackson 反序列化错误: principal 反序列化错误: %s", principalNode.toString());
            log.error(msg);
            throw new IOException(msg);
        }

        final JustAuthAuthenticationToken justAuthAuthenticationToken =
                new JustAuthAuthenticationToken(principal,
                                             tokenAuthorities,
                                             providerId);

        // 创建 details 对象
        if (!(detailsNode.isNull() || detailsNode.isMissingNode())) {
            WebAuthenticationDetails details = mapper.convertValue(detailsNode, new TypeReference<WebAuthenticationDetails>(){});
            justAuthAuthenticationToken.setDetails(details);
        }

        return justAuthAuthenticationToken;

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonDeserialize(using = Auth2AuthenticationTokenJsonDeserializer.class)
    public interface Auth2AuthenticationTokenMixin {}

}
