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
package com.taotao.cloud.common.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.taotao.cloud.common.enums.BaseEnum;
import java.io.IOException;

/**
 * 继承了BaseEnum接口的枚举值，将会统一按照以下格式序列化 { "code": "XX", "desc": "xxx" }
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:20:18
 */
public class EnumSerializer extends StdSerializer<BaseEnum> {

	/**
	 * INSTANCE
	 */
	public static final EnumSerializer INSTANCE = new EnumSerializer();
	/**
	 * ALL_ENUM_KEY_FIELD
	 */
	public static final String ALL_ENUM_KEY_FIELD = "code";
	/**
	 * ALL_ENUM_DESC_FIELD
	 */
	public static final String ALL_ENUM_DESC_FIELD = "desc";

	public EnumSerializer() {
		super(BaseEnum.class);
	}

	@Override
	public void serialize(BaseEnum distance, JsonGenerator generator, SerializerProvider provider)
		throws IOException {
		generator.writeStartObject();
		generator.writeFieldName(ALL_ENUM_KEY_FIELD);
		generator.writeNumber(distance.getCode());
		generator.writeFieldName(ALL_ENUM_DESC_FIELD);
		generator.writeString(distance.getDesc());
		generator.writeEndObject();
	}
}
