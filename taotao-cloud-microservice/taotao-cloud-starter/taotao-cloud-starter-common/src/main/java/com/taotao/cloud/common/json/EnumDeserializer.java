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
package com.taotao.cloud.common.json;

import static com.taotao.cloud.common.json.EnumSerializer.ALL_ENUM_KEY_FIELD;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.taotao.cloud.common.utils.LogUtil;
import org.springframework.beans.BeanUtils;

/**
 * enum反序列化工具
 * <p>
 * 字段类型是枚举类型时，可以按照以下2种格式反序列化： 1. 字符串形式：字段名： "XX" 2. 对象形式： 字段名： { "code": "XX" }
 * <p>
 * 此反序列化类有bug，请使用 com.fasterxml.jackson.databind.deser.std.EnumDeserializer bug1： 不支持接收List
 * bug2: 传错删除没有报错
 *
 * @author shuigedeng
 * @version 1.0.0
 * @see com.fasterxml.jackson.databind.deser.std.EnumDeserializer
 * @since 2021/6/22 17:27
 */
@Deprecated
public class EnumDeserializer extends StdDeserializer<Enum<?>> {

	public static final EnumDeserializer INSTANCE = new EnumDeserializer();


	public EnumDeserializer() {
		super(Enum.class);
	}

	@Override
	public Enum<?> deserialize(JsonParser jp, DeserializationContext context) {
		try {
			// 读取
			JsonNode node = jp.getCodec().readTree(jp);
			// 当前字段
			String currentName = jp.currentName();
			// 当前对象
			Object currentValue = jp.getCurrentValue();
			// 在对象中找到该字段
			Class propertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
			JsonNode code = node.get(ALL_ENUM_KEY_FIELD);
			String val = code != null ? code.asText() : node.asText();
			if (StrUtil.isBlank(val) || StringPool.NULL.equals(val)) {
				return null;
			}
			return Enum.valueOf(propertyType, val);
		} catch (Exception e) {
			LogUtil.error("解析枚举失败", e);
			return null;
		}
	}


}
