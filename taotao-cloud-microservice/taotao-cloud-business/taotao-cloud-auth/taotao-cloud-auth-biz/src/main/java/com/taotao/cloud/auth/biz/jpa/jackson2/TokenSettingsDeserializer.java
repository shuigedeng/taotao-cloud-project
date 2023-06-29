
package com.taotao.cloud.auth.biz.jpa.jackson2;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.auth.biz.utils.JsonNodeUtils;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.io.IOException;
import java.util.Map;

/**
 * <p>Description: TokenSettingsDeserializer </p>
 *
 *
 * @date : 2022/10/24 23:29
 */
public class TokenSettingsDeserializer extends JsonDeserializer<TokenSettings> {

    @Override
    public TokenSettings deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);

        Map<String, Object> settings = JsonNodeUtils.findValue(jsonNode, "settings", JsonNodeUtils.STRING_OBJECT_MAP, mapper);

        return TokenSettings.withSettings(settings).build();
    }
}
