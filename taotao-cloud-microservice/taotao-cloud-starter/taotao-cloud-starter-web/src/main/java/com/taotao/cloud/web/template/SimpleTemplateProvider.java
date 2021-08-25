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
package com.taotao.cloud.web.template;

/**
 * TemplateProvider 缩写简写扩展，方便页面模板里面使用
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:12
 */
public class SimpleTemplateProvider extends TemplateProvider {

	/**
	 * getattr方法 缩写
	 *
	 * @param key key
	 * @return java.lang.Object
	 * @author shuigedeng
	 * @since 2021/8/24 23:12
	 */
	public Object g(String key) {
		return getattr(key);
	}

	/**
	 * setattr方法 缩写
	 *
	 * @param key   key
	 * @param value value
	 * @author shuigedeng
	 * @since 2021/8/24 23:13
	 */
	public void s(String key, Object value) {
		setattr(key, value);
	}

	/**
	 * s2
	 *
	 * @param key   key
	 * @param value value
	 * @return com.taotao.cloud.web.template.SimpleTemplateProvider
	 * @author shuigedeng
	 * @since 2021/8/24 23:13
	 */
	public SimpleTemplateProvider s2(String key, Object value) {
		setattr(key, value);
		return this;
	}

	/**
	 * where 缩写
	 *
	 * @param istrue   istrue
	 * @param trueObj  trueObj
	 * @param falseObj falseObj
	 * @return java.lang.Object
	 * @author shuigedeng
	 * @since 2021/8/24 23:13
	 */
	public Object w(boolean istrue, Object trueObj, Object falseObj) {
		return where(istrue, trueObj, falseObj);
	}

	/**
	 * print 缩写
	 *
	 * @param o o
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2021/8/24 23:13
	 */
	public String p(Object o) {
		return print(o);
	}
}
