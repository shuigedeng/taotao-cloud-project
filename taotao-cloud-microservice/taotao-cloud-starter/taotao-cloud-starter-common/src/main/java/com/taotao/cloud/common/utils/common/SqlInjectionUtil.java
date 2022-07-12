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

package com.taotao.cloud.common.utils.common;

import cn.hutool.core.net.URLDecoder;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * SQL注入处理工具类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-23 08:58:53
 */
public class SqlInjectionUtil {

	private static final String SQL_REGEX = "\\b(and|or)\\b.{1,6}?(=|>|<|\\bin\\b|\\blike\\b)|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)";

	private static final Pattern SQL_PATTERN = Pattern.compile(SQL_REGEX, Pattern.CASE_INSENSITIVE);

	private static boolean matching(String lowerValue, String param) {
		if (SQL_PATTERN.matcher(param).find()) {
			LogUtil.error("The parameter contains keywords {} that do not allow SQL!", lowerValue);
			return true;
		}
		return false;
	}

	private static String toLowerCase(Object obj) {
		//这里需要将参数转换为小写来处理
		return Optional.ofNullable(obj)
			.map(Object::toString)
			.map(String::toLowerCase)
			.orElse("");
	}

	private static boolean checking(Object value) {
		//这里需要将参数转换为小写来处理
		String lowerValue = toLowerCase(value);
		return matching(lowerValue, lowerValue);
	}

	/**
	 * get请求sql注入校验
	 *
	 * @param value 具体的检验
	 * @return 是否存在不合规内容
	 */
	public static boolean checkForGet(String value) {
		//参数需要url编码
		//这里需要将参数转换为小写来处理
		//不改变原值
		//value示例 order=asc&pageNum=1&pageSize=100&parentId=0
		String lowerValue = URLDecoder.decode(value, StandardCharsets.UTF_8).toLowerCase();

		//获取到请求中所有参数值-取每个key=value组合第一个等号后面的值
		return Stream.of(lowerValue.split("\\&"))
			.map(kp -> kp.substring(kp.indexOf("=") + 1))
			.parallel()
			.anyMatch(param -> matching(lowerValue, param));
	}

	/**
	 * post请求sql注入校验
	 *
	 * @param value 具体的检验
	 * @return 是否存在不合规内容
	 */
	public static boolean checkForPost(String value) {

		Object jsonObj = JSON.parse(value);
		if (jsonObj instanceof JSONObject) {
			JSONObject json = (JSONObject) jsonObj;
			return json.entrySet().stream().parallel()
				.anyMatch(entry -> checking(entry.getValue()));
		}

		if (jsonObj instanceof JSONArray) {
			JSONArray json = (JSONArray) jsonObj;
			return json.stream().parallel().anyMatch(SqlInjectionUtil::checking);
		}

		return false;
	}
}
