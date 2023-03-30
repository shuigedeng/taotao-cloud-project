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

package com.taotao.cloud.auth.biz.authentication.oauth2.qq;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.StreamUtils;

/** 自定义消息转换器来解析qq的access_token响应信息 */
public class QqOAuth2AccessTokenResponseHttpMessageConverter
        extends OAuth2AccessTokenResponseHttpMessageConverter {

    public QqOAuth2AccessTokenResponseHttpMessageConverter(MediaType... mediaType) {
        setSupportedMediaTypes(Arrays.asList(mediaType));
    }

    @Override
    protected OAuth2AccessTokenResponse readInternal(
            Class<? extends OAuth2AccessTokenResponse> clazz, HttpInputMessage inputMessage) {

        String response = null;
        try {
            response = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogUtils.info("qq的AccessToken响应信息：{}", response);

        // 解析响应信息类似access_token=YOUR_ACCESS_TOKEN&expires_in=3600这样的字符串
        Map<String, Object> tokenResponseParameters =
                Arrays.stream(response.split("&"))
                        .collect(Collectors.toMap(s -> s.split("=")[0], s -> s.split("=")[1]));

        // 手动给qq的access_token响应信息添加token_type字段，spring-security会按照oauth2规范校验返回参数
        tokenResponseParameters.put(OAuth2ParameterNames.TOKEN_TYPE, "bearer");
        return new DefaultMapOAuth2AccessTokenResponseConverter().convert(tokenResponseParameters);
    }

    @Override
    protected void writeInternal(
            OAuth2AccessTokenResponse tokenResponse, HttpOutputMessage outputMessage) {
        throw new UnsupportedOperationException();
    }
}
