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
 * JavaInfo
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:22:37
 */
public class JavaInfo implements Serializable {

	private final String JAVA_VERSION = System.getProperty("java.version", null);
	private final String JAVA_VENDOR = System.getProperty("java.vendor", null);
	private final String JAVA_VENDOR_URL = System.getProperty("java.vendor.url", null);

	/**
	 * 取得当前Java impl.的版本
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回null
	 * @author shuigedeng
	 * @since 2021-09-02 19:22:49
	 */

	public final String getVersion() {
		return JAVA_VERSION;
	}

	/**
	 * 取得当前Java impl.的厂商
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回null
	 * @author shuigedeng
	 * @since 2021-09-02 19:22:49
	 */
	public final String getVendor() {
		return JAVA_VENDOR;
	}

	/**
	 * 取得当前Java impl.的厂商网站的URL
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回null
	 * @author shuigedeng
	 * @since 2021-09-02 19:22:49
	 */
	public final String getVendorURL() {
		return JAVA_VENDOR_URL;
	}

	/**
	 * 将Java Implementation的信息转换成字符串
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 19:22:49
	 */
	@Override
	public final String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("Java Version:    ").append(getVersion())
			.append("\nJava Vendor:     ").append(getVendor())
			.append("\nJava Vendor URL: ").append(getVendorURL());

		return builder.toString();
	}

}
