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
package com.taotao.cloud.health.strategy;

import java.util.HashMap;

/**
 * 报警模板
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:25:37
 */
public class WarnTemplate extends HashMap<String, String> {

	/**
	 * register
	 *
	 * @param filed    filed
	 * @param template template
	 * @return {@link WarnTemplate }
	 * @since 2022-04-27 17:25:37
	 */
	public WarnTemplate register(String filed, String template) {
		if (!containsKey(filed)) {
			put(filed, template);
		} else {
			replace(filed, template);
		}
		return this;
	}

	/**
	 * getTemplate
	 *
	 * @param filed filed
	 * @return {@link String }
	 * @since 2022-04-27 17:25:37
	 */
	public String getTemplate(String filed) {
		if (!containsKey(filed)) {
			return get("");
		} else {
			return get(filed);
		}
	}

	/**
	 * getWarnContent
	 *
	 * @param filedName filedName
	 * @param filedDesc filedDesc
	 * @param value     value
	 * @param rule      rule
	 * @return {@link String }
	 * @since 2022-04-27 17:25:37
	 */
	public String getWarnContent(String filedName, String filedDesc, Object value,
		Rule.RuleInfo rule) {
		return getTemplate(filedName)
			.replace("{value}", value.toString())
			.replace("{rule}", rule.toString())
			.replace("{name}", filedName)
			.replace("{desc}", filedDesc);
	}
}
