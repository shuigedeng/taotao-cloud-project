/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
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
