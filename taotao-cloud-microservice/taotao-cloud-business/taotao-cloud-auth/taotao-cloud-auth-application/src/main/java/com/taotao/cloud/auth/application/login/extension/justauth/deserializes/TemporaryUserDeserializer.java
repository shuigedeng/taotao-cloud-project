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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.taotao.cloud.auth.application.login.extension.justauth.userdetails.TemporaryUser;
import java.io.IOException;
import java.util.Set;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * TemporaryUser Jackson 反序列化
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/28 17:19
 */
public class TemporaryUserDeserializer extends StdDeserializer<TemporaryUser> {

    public TemporaryUserDeserializer() {
        super(TemporaryUser.class);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public TemporaryUser deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode jsonNode = mapper.readTree(p);

        Set<? extends GrantedAuthority> authorities =
                mapper.convertValue(jsonNode.get("authorities"), new TypeReference<Set<SimpleGrantedAuthority>>() {});
        JsonNode password = this.readJsonNode(jsonNode, "password");
        TemporaryUser result = new TemporaryUser(
                this.readJsonNode(jsonNode, "username").asText(),
                password.asText(""),
                this.readJsonNode(jsonNode, "enabled").asBoolean(),
                this.readJsonNode(jsonNode, "accountNonExpired").asBoolean(),
                this.readJsonNode(jsonNode, "credentialsNonExpired").asBoolean(),
                this.readJsonNode(jsonNode, "accountNonLocked").asBoolean(),
                authorities,
                mapper.convertValue(jsonNode.get("authUser"), new TypeReference<AuthUser>() {}),
                jsonNode.get("encodeState").asText());

        if (password.asText(null) == null) {
            result.eraseCredentials();
        }

        return result;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        if (jsonNode.has(field)) {
            return jsonNode.get(field);
        }
        return MissingNode.getInstance();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonDeserialize(using = TemporaryUserDeserializer.class)
    public interface TemporaryUserMixin {}
}
