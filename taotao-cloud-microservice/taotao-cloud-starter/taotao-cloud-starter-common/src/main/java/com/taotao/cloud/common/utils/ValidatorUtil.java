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
package com.taotao.cloud.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验器：利用正则表达式校验邮箱、手机号等
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 14:37:50
 */
public final class ValidatorUtil {

	private ValidatorUtil() {
	}

	/**
	 * 正则表达式:验证用户名(不包含中文和特殊字符)如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
	 */
	public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";
	/**
	 * 正则表达式:验证密码(不包含特殊字符)
	 */
	public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
	/**
	 * 正则表达式:验证邮箱
	 */
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	/**
	 * 正则表达式:验证汉字(1-9个汉字)  {1,9} 自定义区间
	 */
	public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]{1,9}$";
	/**
	 * 正则表达式:验证身份证
	 */
	public static final String REGEX_ID_CARD = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
	/**
	 * 正则表达式:验证URL
	 */
	public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
	/**
	 * 正则表达式:验证IP地址
	 */
	public static final String REGEX_IP_ADDR = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
	/**
	 * 说明：移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 联通：130、131、132、152、155、156、185、186
	 * 电信：133、153、180、189 虚拟运营商  170 总结起来就是第一位必定为1，第二位必定为3或4或5或7或8，其他位置的可以为0-9 验证号码 手机号 固话均可
	 * 正则表达式:验证手机号
	 */
	private static final String REGEX_MOBILE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
	/**
	 * pattern
	 */
	private static final Pattern PATTERN = Pattern.compile(REGEX_MOBILE);

	/**
	 * 校验用户名
	 *
	 * @param username 用户名
	 * @return boolean  校验通过返回true，否则返回false
	 * @author shuigedeng
	 * @since 2021-09-02 14:38:30
	 */
	public static boolean isUserName(String username) {
		return Pattern.matches(REGEX_USERNAME, username);
	}

	/**
	 * 校验密码
	 *
	 * @param password 密码
	 * @return boolean  校验通过返回true，否则返回false
	 * @author shuigedeng
	 * @since 2021-09-02 14:38:57
	 */
	public static boolean isPassword(String password) {
		return Pattern.matches(REGEX_PASSWORD, password);
	}

	/**
	 * 校验手机号
	 *
	 * @param phone 手机号
	 * @return boolean  是否校验成功
	 * @author shuigedeng
	 * @since 2021-09-02 14:39:07
	 */
	public static boolean checkPhone(String phone) {
		//noinspection AlibabaUndefineMagicConstant
		if (phone == null || phone.length() != 11) {
			return Boolean.FALSE;
		}

		Matcher m = PATTERN.matcher(phone);
		return m.matches();
	}

}
