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
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 安全表达式
 *
 * @author : gengwei.zheng
 * @date : 2021/8/14 3:49
 */
@Schema(title = "Security 权限表达式")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PermissionExpression implements BaseUiEnum<String> {
    /** 权限表达式 */
    PERMIT_ALL("permitAll", "permitAll"),
    ANONYMOUS("anonymous", "anonymous"),
    REMEMBER_ME("rememberMe", "rememberMe"),
    DENY_ALL("denyAll", "denyAll"),
    AUTHENTICATED("authenticated", "authenticated"),
    FULLY_AUTHENTICATED("fullyAuthenticated", "fullyAuthenticated"),
    NOT_PERMIT_ALL("!permitAll", "!permitAll"),
    NOT_ANONYMOUS("!anonymous", "!anonymous"),
    NOT_REMEMBER_ME("!rememberMe", "!rememberMe"),
    NOT_DENY_ALL("!denyAll", "!denyAll"),
    NOT_AUTHENTICATED("!authenticated", "!authenticated"),
    NOT_FULLY_AUTHENTICATED("!fullyAuthenticated", "!fullyAuthenticated"),
    HAS_ROLE("hasRole", "hasRole"),
    HAS_ANY_ROLE("hasAnyRole", "hasAnyRole"),
    HAS_AUTHORITY("hasAuthority", "hasAuthority"),
    HAS_ANY_AUTHORITY("hasAnyAuthority", "hasAnyAuthority"),
    HAS_IP_ADDRESS("hasIpAddress", "hasIpAddress");

    private static final Map<String, PermissionExpression> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> JSON_STRUCTURE = new ArrayList<>();

    @Schema(title = "索引")
    private final String value;

    @Schema(title = "说明")
    private final String description;

    static {
        for (PermissionExpression permissionExpression : PermissionExpression.values()) {
            INDEX_MAP.put(permissionExpression.getValue(), permissionExpression);
            JSON_STRUCTURE.add(
                    permissionExpression.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", permissionExpression.getValue())
                            .put("key", permissionExpression.name())
                            .put("text", permissionExpression.getDescription())
                            .put("index", permissionExpression.ordinal())
                            .build());
        }
    }

    PermissionExpression(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static PermissionExpression get(String value) {
        return INDEX_MAP.get(value);
    }

    public static List<Map<String, Object>> getPreprocessedJsonStructure() {
        return JSON_STRUCTURE;
    }
}
