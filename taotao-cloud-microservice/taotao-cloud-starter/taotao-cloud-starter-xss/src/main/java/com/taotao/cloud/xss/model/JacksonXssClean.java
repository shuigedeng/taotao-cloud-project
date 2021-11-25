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

package com.taotao.cloud.xss.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.xss.properties.XssProperties;
import com.taotao.cloud.xss.utils.XssUtil;
import java.io.IOException;

/**
 * jackson xss 处理
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
public class JacksonXssClean extends JsonDeserializer<String> {

	private final XssProperties properties;
	private final XssCleaner xssCleaner;

	public JacksonXssClean(XssProperties properties, XssCleaner xssCleaner) {
		this.properties = properties;
		this.xssCleaner = xssCleaner;
	}

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
		// XSS filter
		String text = p.getValueAsString();
		if (text == null) {
			return null;
		}

		if (XssHolder.isEnabled()) {
			String value = xssCleaner.clean(XssUtil.trim(text, properties.getTrimText()));
			LogUtil.debug("Json property value:{} cleaned up by mica-xss, current value is:{}.",
				text,
				value);
			return value;
		} else {
			return XssUtil.trim(text, properties.getTrimText());
		}
	}

}
