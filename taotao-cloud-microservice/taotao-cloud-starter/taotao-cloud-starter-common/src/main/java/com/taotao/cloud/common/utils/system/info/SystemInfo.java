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
package com.taotao.cloud.common.utils.system.info;

import java.io.Serializable;

/**
 * SystemInfo
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:26:50
 */
public class SystemInfo implements Serializable {

	private SystemInfo() {
	}

	public static final UserInfo userInfo = new UserInfo();
	public static final OsInfo osInfo = new OsInfo();
	public static final JavaInfo javaInfo = new JavaInfo();
	public static final JvmInfo jvmInfo = new JvmInfo();
	public static final RuntimeInfo runtimeInfo = new RuntimeInfo();

	/**
	 * info
	 *
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 19:26:58
	 */
	public static String info() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[UserInfo]\n").append(userInfo.toString()).append("\n")
			.append("\n[OsInfo]\n").append(osInfo.toString()).append("\n")
			.append("\n[JavaInfo]\n").append(javaInfo.toString()).append("\n")
			.append("\n[JvmInfo]\n").append(jvmInfo.toString()).append("\n")
			.append("\n[RuntimeInfo]\n").append(runtimeInfo.toString()).append("\n");

		return stringBuilder.toString();
	}

}
