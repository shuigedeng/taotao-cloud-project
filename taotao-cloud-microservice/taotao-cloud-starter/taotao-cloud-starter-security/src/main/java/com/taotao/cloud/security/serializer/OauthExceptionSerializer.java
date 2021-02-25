/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.security.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.taotao.cloud.security.exception.CustomOauthException;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * CustomOauthExceptionSerializer
 *
 * @author dengtao
 * @date 2020/4/30 09:12
 * @since v1.0
 */
public class OauthExceptionSerializer extends StdSerializer<CustomOauthException> {

    public OauthExceptionSerializer() {
        super(CustomOauthException.class);
    }

    @Override
    public void serialize(CustomOauthException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("code", value.getHttpErrorCode());
        String errorMessage = value.getErrorCode();
        if (errorMessage != null) {
            errorMessage = HtmlUtils.htmlEscape(errorMessage);
        }
        gen.writeStringField("message", errorMessage);
        gen.writeStringField("data", null);
        gen.writeEndObject();
    }
}
