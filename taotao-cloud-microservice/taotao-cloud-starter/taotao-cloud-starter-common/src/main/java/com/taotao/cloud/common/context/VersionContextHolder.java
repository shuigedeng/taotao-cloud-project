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
package com.taotao.cloud.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 负载均衡规则Holder
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:34:15
 */
public class VersionContextHolder {

	private VersionContextHolder() {
	}

	/**
	 * VERSION_CONTEXT
	 */
	private static final ThreadLocal<String> VERSION_CONTEXT = new TransmittableThreadLocal<>();

	/**
	 * setVersion
	 *
	 * @param version version
	 * @since 2021-09-02 19:34:26
	 */
	public static void setVersion(String version) {
		VERSION_CONTEXT.set(version);
	}

	/**
	 * getVersion
	 *
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 19:34:28
	 */
	public static String getVersion() {
		return VERSION_CONTEXT.get();
	}

	/**
	 * clear
	 *
	 * @since 2021-09-02 19:34:32
	 */
	public static void clear() {
		VERSION_CONTEXT.remove();
	}
}
