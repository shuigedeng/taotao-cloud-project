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
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonDeserializer;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.MissingNode;
import com.taotao.boot.security.spring.authentication.login.form.FormLoginWebAuthenticationDetails;
import java.io.IOException;

/**
 * <p>FormLoginWebAuthenticationDetailsDeserializer </p>
 *
 *
 * @since : 2022/4/14 11:48
 */
public class FormLoginWebAuthenticationDetailsDeserializer
        extends JsonDeserializer<FormLoginWebAuthenticationDetails> {
    @Override
    public FormLoginWebAuthenticationDetails deserialize(
            JsonParser jp, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);

        String remoteAddress = readJsonNode(jsonNode, "remoteAddress").asText();
        String sessionId = readJsonNode(jsonNode, "sessionId").asText();
        String parameterName = readJsonNode(jsonNode, "parameterName").asText();
        String category = readJsonNode(jsonNode, "category").asText();
        String code = readJsonNode(jsonNode, "code").asText();
        String identity = readJsonNode(jsonNode, "identity").asText();
        boolean closed = readJsonNode(jsonNode, "closed").asBoolean();

        return new FormLoginWebAuthenticationDetails(
                remoteAddress, sessionId, closed, parameterName, category, code, identity);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
