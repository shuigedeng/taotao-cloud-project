/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.core.enums;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;
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
public enum GrantType {

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
