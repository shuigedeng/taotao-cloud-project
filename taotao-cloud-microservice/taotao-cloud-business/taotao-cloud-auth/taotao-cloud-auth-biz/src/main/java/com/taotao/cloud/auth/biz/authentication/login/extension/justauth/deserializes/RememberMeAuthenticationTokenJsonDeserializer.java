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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * RememberMeAuthenticationToken Jackson 反序列化
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/28 10:58
 */
public class RememberMeAuthenticationTokenJsonDeserializer extends StdDeserializer<RememberMeAuthenticationToken> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public RememberMeAuthenticationTokenJsonDeserializer() {
        super(RememberMeAuthenticationToken.class);
    }

    @Override
    public RememberMeAuthenticationToken deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        final JsonNode jsonNode = mapper.readTree(p);

        // 获取 authorities
        Collection<? extends GrantedAuthority> tokenAuthorities = mapper.convertValue(
                jsonNode.get("authorities"), new TypeReference<Collection<SimpleGrantedAuthority>>() {});

        final boolean authenticated = jsonNode.get("authenticated").asBoolean();
        final Integer keyHash = jsonNode.get("keyHash").asInt();
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
                    "RememberMeAuthenticationToken Jackson 反序列化错误: principal 反序列化错误: %s", principalNode.toString());
            log.error(msg);
            throw new IOException(msg);
        }

        // 创建 RememberMeAuthenticationToken 对象
        RememberMeAuthenticationToken token;
        try {
            final Constructor<RememberMeAuthenticationToken> declaredConstructor =
                    RememberMeAuthenticationToken.class.getDeclaredConstructor(
                            Integer.class, Object.class, Collection.class);
            declaredConstructor.setAccessible(true);
            token = declaredConstructor.newInstance(keyHash, principal, tokenAuthorities);
        } catch (NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            final String msg =
                    String.format("RememberMeAuthenticationToken Jackson 反序列化错误: principal 反序列化错误: %s", e.getMessage());
            log.error(msg);
            throw new IOException(msg);
        }

        token.setAuthenticated(authenticated);
        // 为了安全, 不信任反序列化后的凭证; 一般认证成功后都会自动释放密码.
        token.eraseCredentials();

        // 创建 details 对象
        if (!(detailsNode.isNull() || detailsNode.isMissingNode())) {
            WebAuthenticationDetails details =
                    mapper.convertValue(detailsNode, new TypeReference<WebAuthenticationDetails>() {});
            token.setDetails(details);
        }

        return token;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonDeserialize(using = RememberMeAuthenticationTokenJsonDeserializer.class)
    public interface RememberMeAuthenticationTokenMixin {}
}
