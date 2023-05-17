/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.jpa.jackson2;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.taotao.cloud.auth.biz.dante.core.definition.details.FormLoginWebAuthenticationDetails;

import java.io.IOException;

/**
 * <p>Description: FormLoginWebAuthenticationDetailsDeserializer </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/14 11:48
 */
public class FormLoginWebAuthenticationDetailsDeserializer extends JsonDeserializer<FormLoginWebAuthenticationDetails> {
    @Override
    public FormLoginWebAuthenticationDetails deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);

        String remoteAddress = readJsonNode(jsonNode, "remoteAddress").asText();
        String sessionId = readJsonNode(jsonNode, "sessionId").asText();
        String parameterName = readJsonNode(jsonNode, "parameterName").asText();
        String category = readJsonNode(jsonNode, "category").asText();
        String code = readJsonNode(jsonNode, "code").asText();
        String identity = readJsonNode(jsonNode, "identity").asText();
        boolean closed = readJsonNode(jsonNode, "closed").asBoolean();

        return new FormLoginWebAuthenticationDetails(remoteAddress, sessionId, closed, parameterName, category, code, identity);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
