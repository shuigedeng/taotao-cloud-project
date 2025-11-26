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

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.Module;
import tools.jackson.databind.json.JsonMapper;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.ClassUtils;

/**
 * <p>OAuth2 相关 Jackson 处理器 </p>
 *
 *
 * @since : 2023/4/29 16:05
 */
public class OAuth2JacksonProcessor {

    private static final Logger log = LoggerFactory.getLogger(OAuth2JacksonProcessor.class);

    private final JsonMapper objectMapper;

    public OAuth2JacksonProcessor() {

        objectMapper = new JsonMapper();

        ClassLoader classLoader = OAuth2JacksonProcessor.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);

        Module module =
                loadAndGetInstance(
                        "com.taotao.cloud.auth.biz.jpa.jackson2.FormOAuth2PhoneLoginJackson2Module",
                        classLoader);
        securityModules.add(module);

        objectMapper.registerModules(securityModules);
        objectMapper.registerModules(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.registerModules(new TtcJackson2Module());
        objectMapper.registerModules(new OAuth2TokenJackson2Module());
    }

    private static Module loadAndGetInstance(String className, ClassLoader loader) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Module> securityModule =
                    (Class<? extends Module>) ClassUtils.forName(className, loader);
            return securityModule.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {
        }
        return null;
    }

    public Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<>() {});
        } catch (Exception ex) {
            log.error("OAuth2 jackson processing parseMap catch error {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    public String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.error("OAuth2 jackson processing writeMap catch error {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
