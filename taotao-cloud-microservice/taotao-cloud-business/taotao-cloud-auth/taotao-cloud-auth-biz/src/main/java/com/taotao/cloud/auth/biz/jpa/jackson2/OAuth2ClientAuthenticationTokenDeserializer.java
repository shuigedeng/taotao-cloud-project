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

package com.taotao.cloud.auth.biz.jpa.jackson2;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonDeserializer;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.JsonMapper;
import com.taotao.boot.security.spring.core.authority.TtcGrantedAuthority;
import com.taotao.cloud.auth.biz.utils.JsonNodeUtils;
import java.io.IOException;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * <p>OAuth2ClientAuthenticationTokenDeserializer </p>
 *
 *
 * @since : 2022/10/24 14:43
 */
public class OAuth2ClientAuthenticationTokenDeserializer
        extends JsonDeserializer<OAuth2ClientAuthenticationToken> {

    private static final TypeReference<Set<TtcGrantedAuthority>> TTC_GRANTED_AUTHORITY_SET =
            new TypeReference<Set<TtcGrantedAuthority>>() {};

    @Override
    public OAuth2ClientAuthenticationToken deserialize(
            JsonParser jsonParser, DeserializationContext context)
            throws IOException, JacksonException {

        JsonMapper mapper = (JsonMapper) jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);
        return deserialize(jsonParser, mapper, jsonNode);
    }

    private OAuth2ClientAuthenticationToken deserialize(
            JsonParser parser, JsonMapper mapper, JsonNode root) throws IOException {
        Set<TtcGrantedAuthority> authorities =
                JsonNodeUtils.findValue(root, "authorities", TTC_GRANTED_AUTHORITY_SET, mapper);
        RegisteredClient registeredClient =
                JsonNodeUtils.findValue(
                        root, "registeredClient", new TypeReference<RegisteredClient>() {}, mapper);
        String credentials = JsonNodeUtils.findStringValue(root, "credentials");
        ClientAuthenticationMethod clientAuthenticationMethod =
                JsonNodeUtils.findValue(
                        root,
                        "clientAuthenticationMethod",
                        new TypeReference<ClientAuthenticationMethod>() {},
                        mapper);

        OAuth2ClientAuthenticationToken clientAuthenticationToken =
                new OAuth2ClientAuthenticationToken(
                        registeredClient, clientAuthenticationMethod, credentials);
        if (CollectionUtils.isNotEmpty(authorities)) {
            FieldUtil.setFieldValue(clientAuthenticationToken, "authorities", authorities);
        }
        return clientAuthenticationToken;
    }
}
