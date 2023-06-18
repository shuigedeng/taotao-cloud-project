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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.userdetails.TemporaryUser;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Set;

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
			mapper.convertValue(jsonNode.get("authorities"), new TypeReference<Set<SimpleGrantedAuthority>>() {
			});
		JsonNode password = this.readJsonNode(jsonNode, "password");
		TemporaryUser result = new TemporaryUser(this.readJsonNode(jsonNode, "username").asText(),
			password.asText(""),
			this.readJsonNode(jsonNode, "enabled").asBoolean(),
			this.readJsonNode(jsonNode, "accountNonExpired").asBoolean(),
			this.readJsonNode(jsonNode, "credentialsNonExpired").asBoolean(),
			this.readJsonNode(jsonNode, "accountNonLocked").asBoolean(),
			authorities,
			mapper.convertValue(jsonNode.get("authUser"),
				new TypeReference<AuthUser>() {
				}),
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
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
	@JsonDeserialize(using = TemporaryUserDeserializer.class)
	public interface TemporaryUserMixin {
	}

}
