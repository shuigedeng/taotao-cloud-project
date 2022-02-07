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
package com.taotao.cloud.common.constant;

/**
 * RedisConstant
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:35:46
 */
public class RedisConstant {

	/**
	 * 用户退出时JWT标识KEY
	 */
	public static final String LOGOUT_JWT_KEY_PREFIX = "LOGOUT:JWT:KEY:";

	/**
	 * 图形验证码KEY
	 */
	public static final String CAPTCHA_KEY_PREFIX = "CAPTCHA:KEY:";

	/**
	 * 短信验证码KEY
	 */
	public static final String SMS_KEY_PREFIX = "SMS:KEY:";

	/**
	 * 短信校验码KEY
	 */
	public static final String SMS_VERIFICATION_CODE_KEY_PREFIX = "SMS:VERIFICATION:CODE:KEY:";

	/**
	 * 分布式锁key
	 */
	public static final String LOCK_KEY_PREFIX = "LOCK:KEY:";

	/**
	 * 敏感词key
	 */
	public static final String SENSITIVE_WORDS_KEY = "SENSITIVE:WORDS:KEY";


	/**
	 * regions patten key
	 */
	public static final String REGIONS_PATTERN = "REGIONS:";
	/**
	 * regions key
	 */
	public static final String REGIONS_KEY = "REGIONS:KEY";
	/**
	 * regions all city key
	 */
	public static final String REGIONS_ALL_CITY_KEY = "REGIONS:ALL:CITY:KEY";
	/**
	 * region parent id key
	 */
	public static final String REGIONS_PARENT_ID_KEY = "REGIONS:PARENT:ID:KEY:";
}
