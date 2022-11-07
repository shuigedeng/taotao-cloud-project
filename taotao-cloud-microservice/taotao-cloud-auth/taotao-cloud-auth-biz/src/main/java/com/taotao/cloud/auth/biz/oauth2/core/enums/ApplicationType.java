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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 应用类别 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/5/4 12:01
 */
@Schema(title = "应用类型")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApplicationType {

	/**
	 * 应用类型
	 */
	WEB(0, "PC网页应用"),
	SERVICE(1, "服务应用"),
	APP(2, "手机应用"),
	WAP(3, "手机网页应用"),
	MINI(4, "小程序应用");

	@Schema(title = "枚举值")
	private final Integer value;
	@Schema(title = "文字")
	private final String description;

	private static final Map<Integer, ApplicationType> INDEX_MAP = new HashMap<>();
	private static final List<Map<String, Object>> JSON_STRUCT = new ArrayList<>();

	static {
		for (ApplicationType applicationType : ApplicationType.values()) {
			INDEX_MAP.put(applicationType.getValue(), applicationType);
			JSON_STRUCT.add(applicationType.getValue(),
				ImmutableMap.<String, Object>builder()
					.put("value", applicationType.getValue())
					.put("key", applicationType.name())
					.put("text", applicationType.getDescription())
					.put("index", applicationType.getValue())
					.build());
		}
	}

	ApplicationType(Integer value, String description) {
		this.value = value;
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * 不加@JsonValue，转换的时候转换出完整的对象。 加了@JsonValue，只会显示相应的属性的值
	 * <p>
	 * 不使用@JsonValue @JsonDeserializer类里面要做相应的处理
	 *
	 * @return Enum枚举值
	 */
	@JsonValue
	@Override
	public Integer getValue() {
		return value;
	}

	public static ApplicationType get(Integer index) {
		return INDEX_MAP.get(index);
	}

	public static List<Map<String, Object>> getPreprocessedJsonStructure() {
		return JSON_STRUCT;
	}
}
