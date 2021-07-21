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
package com.taotao.cloud.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 负载均衡规则Holder
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:25
 */
public class VersionContextHolder {

	private VersionContextHolder() {
	}

	private static final ThreadLocal<String> VERSION_CONTEXT = new TransmittableThreadLocal<>();

	public static void setVersion(String version) {
		VERSION_CONTEXT.set(version);
	}

	public static String getVersion() {
		return VERSION_CONTEXT.get();
	}

	public static void clear() {
		VERSION_CONTEXT.remove();
	}
}
