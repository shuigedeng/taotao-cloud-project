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
package com.taotao.cloud.common.info;

import java.io.Serializable;

/**
 * UserInfo
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/27 20:46
 */
public class UserInfo implements Serializable {

	private final String USER_NAME = System.getProperty("user.name", null);
	private final String USER_HOME = System.getProperty("user.home", null);
	private final String USER_DIR = System.getProperty("user.dir", null);
	private final String USER_LANGUAGE = System.getProperty("user.language", null);
	private final String USER_COUNTRY = ((System.getProperty("user.country", null) == null)
		? System.getProperty("user.region", null) : System.getProperty("user.country", null));
	private final String JAVA_IO_TMPDIR = System.getProperty("java.io.tmpdir", null);

	/**
	 * 取得当前登录用户的名字
	 */
	public final String getName() {
		return USER_NAME;
	}

	/**
	 * 取得当前登录用户的home目录
	 */
	public final String getHomeDir() {
		return USER_HOME;
	}

	/**
	 * 取得当前目录
	 */
	public final String getCurrentDir() {
		return USER_DIR;
	}

	/**
	 * 取得临时目录
	 */
	public final String getTempDir() {
		return JAVA_IO_TMPDIR;
	}

	/**
	 * 取得当前登录用户的语言设置
	 */
	public final String getLanguage() {
		return USER_LANGUAGE;
	}

	/**
	 * 取得当前登录用户的国家或区域设置
	 */
	public final String getCountry() {
		return USER_COUNTRY;
	}

	/**
	 * 将当前用户的信息转换成字符串。
	 */
	@Override
	public final String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("User Name:           ").append(getName())
			.append("\nUser Home Dir:       ").append(getHomeDir())
			.append("\nUser Current Dir:    ").append(getCurrentDir())
			.append("\nUser Temp Dir:       ").append(getTempDir())
			.append("\nUser Language:       ").append(getLanguage())
			.append("\nUser Country:        ").append(getCountry());

		return builder.toString();
	}

}
