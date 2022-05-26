/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2021 Zhenggengwei<码匠君>, herodotus@aliyun.com
 *
 * This file is part of Herodotus Cloud.
 *
 * Herodotus Cloud is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Cloud is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with with Herodotus Cloud;
 * if no see <https://gitee.com/herodotus/herodotus-cloud>
 *
 * - Author: Zhenggengwei<码匠君>
 * - Contact: herodotus@aliyun.com
 * - License: GNU Lesser General Public License (LGPL)
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package com.taotao.cloud.auth.biz.herodotus.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;
import com.taotao.cloud.auth.biz.herodotus.core.constants.BaseConstants;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: OAuth2 认证模式枚举 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/16 14:39
 */
@Schema(title = "OAuth2 认证模式")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GrantType implements BaseUiEnum<String> {

    /**
     * enum
     */
    AUTHORIZATION_CODE(BaseConstants.AUTHORIZATION_CODE, "Authorization Code 模式"),
    PASSWORD(BaseConstants.PASSWORD, "Password 模式"),
    CLIENT_CREDENTIALS(BaseConstants.CLIENT_CREDENTIALS, "Client Credentials 模式"),
    REFRESH_TOKEN(BaseConstants.REFRESH_TOKEN, "Refresh Token 模式"),
    SOCIAL_CREDENTIALS(BaseConstants.SOCIAL_CREDENTIALS, "Social Credentials 模式");

    @Schema(title = "认证模式")
    private final String value;
    @Schema(title = "文字")
    private final String description;

    private static final Map<Integer, GrantType> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> JSON_STRUCTURE = new ArrayList<>();

    static {
        for (GrantType grantType : GrantType.values()) {
            INDEX_MAP.put(grantType.ordinal(), grantType);
            JSON_STRUCTURE.add(grantType.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", grantType.getValue())
                            .put("key", grantType.name())
                            .put("text", grantType.getDescription())
                            .put("index", grantType.ordinal())
                            .build());
        }
    }

    GrantType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static GrantType get(Integer index) {
        return INDEX_MAP.get(index);
    }

    public static List<Map<String, Object>> getPreprocessedJsonStructure() {
        return JSON_STRUCTURE;
    }
}
