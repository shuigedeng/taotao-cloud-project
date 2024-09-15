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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.jackson2;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.boot.security.spring.utils.JsonNodeUtils;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

/**
 * <p>TokenSettingsDeserializer </p>
 *
 *
 * @since : 2022/10/24 23:29
 */
public class TokenSettingsDeserializer extends JsonDeserializer<TokenSettings> {

    @Override
    public TokenSettings deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);

        Map<String, Object> settings =
                JsonNodeUtils.findValue(jsonNode, "settings", JsonNodeUtils.STRING_OBJECT_MAP, mapper);

        return TokenSettings.withSettings(settings).build();
    }
}
