/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.captcha.service;

/**
 * 验证码缓存接口
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:56:54
 */
public interface CaptchaCacheService {

	/**
	 * set
	 *
	 * @param key              key
	 * @param value            value
	 * @param expiresInSeconds expiresInSeconds
	 * @since 2021-09-03 20:56:56
	 */
	void set(String key, String value, long expiresInSeconds);

	/**
	 * exists
	 *
	 * @param key key
	 * @return boolean
	 * @since 2021-09-03 20:56:59
	 */
	boolean exists(String key);

	/**
	 * delete
	 *
	 * @param key key
	 * @since 2021-09-03 20:57:02
	 */
	void delete(String key);

	/**
	 * get
	 *
	 * @param key key
	 * @return {@link java.lang.String }
	 * @since 2021-09-03 20:57:04
	 */
	String get(String key);

	/**
	 * 缓存类型-local/redis/memcache/.. 通过java SPI机制，接入方可自定义实现类
	 *
	 * @return {@link java.lang.String }
	 * @since 2021-09-03 20:57:11
	 */
	String type();

	/**
	 * increment
	 *
	 * @param key key
	 * @param val val
	 * @return {@link java.lang.Long }
	 * @since 2021-09-03 20:57:15
	 */
	default Long increment(String key, long val) {
		return 0L;
	}

}
