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
package com.taotao.cloud.common.utils.system.info;

import java.io.Serializable;

/**
 * JvmInfo
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:23:34
 */
public class JvmInfo implements Serializable {

	private final String JAVA_VM_NAME = System.getProperty("java.vm.name", null);
	private final String JAVA_VM_VERSION = System.getProperty("java.vm.version", null);
	private final String JAVA_VM_VENDOR = System.getProperty("java.vm.vendor", null);
	private final String JAVA_VM_INFO = System.getProperty("java.vm.info", null);

	/**
	 * 取得当前JVM的名称
	 *
	 * @since 2021-09-02 19:23:47
	 */
	public final String getName() {
		return JAVA_VM_NAME;
	}

	/**
	 * 取得当前JVM的版本
	 *
	 * @since 2021-09-02 19:23:47
	 */
	public final String getVersion() {
		return JAVA_VM_VERSION;
	}

	/**
	 * 取得当前JVM的厂商
	 *
	 * @since 2021-09-02 19:23:47
	 */
	public final String getVendor() {
		return JAVA_VM_VENDOR;
	}

	/**
	 * 取得当前JVM的信息
	 *
	 * @since 2021-09-02 19:23:47
	 */
	public final String getInfo() {
		return JAVA_VM_INFO;
	}

	@Override
	public final String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("JavaVM Name:    ").append(getName())
			.append("\nJavaVM Version: ").append(getVersion())
			.append("\nJavaVM Vendor:  ").append(getVendor())
			.append("\nJavaVM Info:    ").append(getInfo());

		return builder.toString();
	}

}
