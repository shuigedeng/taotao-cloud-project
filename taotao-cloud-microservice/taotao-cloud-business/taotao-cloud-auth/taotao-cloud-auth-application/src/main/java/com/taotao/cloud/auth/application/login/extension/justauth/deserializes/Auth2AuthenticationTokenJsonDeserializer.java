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

package com.taotao.cloud.auth.application.login.extension.justauth.deserializes;

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
import com.taotao.cloud.auth.application.login.extension.justauth.JustAuthAuthenticationToken;
import java.io.IOException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

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
        Collection<? extends GrantedAuthority> tokenAuthorities = mapper.convertValue(
                jsonNode.get("authorities"), new TypeReference<Collection<SimpleGrantedAuthority>>() {});

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
        } catch (Exception e) {
            final String msg = String.format(
                    "Auth2AuthenticationToken Jackson 反序列化错误: principal 反序列化错误: %s", principalNode.toString());
            log.error(msg);
            throw new IOException(msg);
        }

        final JustAuthAuthenticationToken justAuthAuthenticationToken =
                new JustAuthAuthenticationToken(principal, tokenAuthorities, providerId);

        // 创建 details 对象
        if (!(detailsNode.isNull() || detailsNode.isMissingNode())) {
            WebAuthenticationDetails details =
                    mapper.convertValue(detailsNode, new TypeReference<WebAuthenticationDetails>() {});
            justAuthAuthenticationToken.setDetails(details);
        }

        return justAuthAuthenticationToken;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonDeserialize(using = Auth2AuthenticationTokenJsonDeserializer.class)
    public interface Auth2AuthenticationTokenMixin {}
}
