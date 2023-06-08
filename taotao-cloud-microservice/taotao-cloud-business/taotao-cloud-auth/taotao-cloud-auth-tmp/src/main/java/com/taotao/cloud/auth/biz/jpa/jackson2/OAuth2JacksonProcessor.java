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

package com.taotao.cloud.auth.biz.jpa.jackson2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: OAuth2 相关 Jackson 处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/4/29 16:05
 */
public class OAuth2JacksonProcessor {

    private static final Logger log = LoggerFactory.getLogger(OAuth2JacksonProcessor.class);

    private final ObjectMapper objectMapper;

    public OAuth2JacksonProcessor() {

        objectMapper = new ObjectMapper();

        ClassLoader classLoader = OAuth2JacksonProcessor.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);

        objectMapper.registerModules(securityModules);
        objectMapper.registerModules(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.registerModules(new HerodotusJackson2Module());
        objectMapper.registerModules(new OAuth2TokenJackson2Module());
    }

    public Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<>() {
            });
        } catch (Exception ex) {
            log.error("[Herodotus] |- OAuth2 jackson processing parseMap catch error {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    public String writeMap(Map<String, Object> data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.error("[Herodotus] |- OAuth2 jackson processing writeMap catch error {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
