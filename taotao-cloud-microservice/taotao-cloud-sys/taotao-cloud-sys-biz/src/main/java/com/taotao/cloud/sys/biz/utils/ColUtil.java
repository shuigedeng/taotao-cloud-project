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
package com.taotao.cloud.sys.biz.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * sql字段转java
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:16:28
 */
public class ColUtil {

	/**
	 * 转换mysql数据类型为java数据类型
	 *
	 * @param type 数据库字段类型
	 * @return java数据类型
	 */
	static String cloToJava(String type) {
		Configuration config = getConfig();
		assert config != null;
		return config.getString(type, "unknowType");
	}

	/**
	 * 获取配置信息
	 *
	 * @return 配置信息
	 * @since 2022-03-25 14:16:53
	 */
	public static PropertiesConfiguration getConfig() {
		try {
			return new PropertiesConfiguration("generator.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
