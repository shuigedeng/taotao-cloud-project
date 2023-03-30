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

package com.taotao.cloud.auth.biz.demo.core.enums;

import cn.herodotus.engine.assistant.core.definition.enums.BaseUiEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * Description: 客户端身份验证模式
 *
 * @author : gengwei.zheng
 * @date : 2022/3/17 14:49
 */
@Schema(title = "OAuth2 Client 认证方式")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuthenticationMethod implements BaseUiEnum<String> {

    /** enum */
    CLIENT_SECRET_BASIC(
            ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue(), "基于Client Secret的Basic验证模式"),
    CLIENT_SECRET_POST(
            ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue(), "基于Client Secret的Post验证模式"),
    CLIENT_SECRET_JWT(
            ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue(), "基于Client Secret的JWT验证模式"),
    PRIVATE_KEY_JWT(ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue(), "基于私钥的JWT验证模式"),
    NONE(ClientAuthenticationMethod.NONE.getValue(), "不设置任何模式");

    @Schema(title = "认证方法")
    private final String value;

    @Schema(title = "文字")
    private final String description;

    private static final Map<Integer, AuthenticationMethod> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> JSON_STRUCTURE = new ArrayList<>();

    static {
        for (AuthenticationMethod authenticationMethod : AuthenticationMethod.values()) {
            INDEX_MAP.put(authenticationMethod.ordinal(), authenticationMethod);
            JSON_STRUCTURE.add(
                    authenticationMethod.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", authenticationMethod.getValue())
                            .put("key", authenticationMethod.name())
                            .put("text", authenticationMethod.getDescription())
                            .put("index", authenticationMethod.ordinal())
                            .build());
        }
    }

    AuthenticationMethod(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static AuthenticationMethod get(Integer index) {
        return INDEX_MAP.get(index);
    }

    public static List<Map<String, Object>> getPreprocessedJsonStructure() {
        return JSON_STRUCTURE;
    }
}
