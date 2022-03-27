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

import com.taotao.cloud.common.utils.common.JsonUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 底层模板方法的扩展，用于前台freemarker的java函数扩展，方便使用。 但是使用前要么在controller层（SpringMvcController）进行重新注入，
 * 要么在类似NetMvcHandlerInterceptor拦截器方式注入重载
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:16:52
 */
public class HtmlHelper extends SimpleTemplateProvider {

	/**
	 * substring3
	 *
	 * @param str    str
	 * @param maxlen maxlen
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 22:17:02
	 */
	public String substring3(String str, int maxlen) {
		return this.cutString(str, maxlen);
	}

	/**
	 * totalPageNum
	 *
	 * @param totalRecord totalRecord
	 * @param pageSize    pageSize
	 * @return int
	 * @since 2021-09-02 22:17:07
	 */
	public int totalPageNum(int totalRecord, int pageSize) {
		return (totalRecord + pageSize - 1) / pageSize + 1;
	}

	/**
	 * help
	 *
	 * @param str str
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 22:17:11
	 */
	public String help(String str) {
		return String.format(
				"<img class='texthelp' width=\"20\" height=\"20\" title=\"%s\" style=\"\" src=\"/content/images/help.png\">",
				str);
	}

	/**
	 * isnull
	 *
	 * @param o o
	 * @return {@link java.lang.Boolean }
	 * @since 2021-09-02 22:17:16
	 */
	public Boolean isnull(Object o) {
		return o == null;
	}

	/**
	 * empty
	 *
	 * @param o o
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 22:17:21
	 */
	public Object empty(Object o) {
		if (o == null || o.equals("null")) {
			return "";
		}
		return o;
	}


	/**
	 * defaultValue
	 *
	 * @param o            o
	 * @param defaultValue defaultValue
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 22:17:25
	 */
	public Object defaultValue(Object o, Object defaultValue) {
		if (o == null) {
			return defaultValue;
		} else {
			return o;
		}
	}

	/**
	 * w2
	 *
	 * @param condition condition
	 * @param data1     data1
	 * @param data2     data2
	 * @param trueObj   trueObj
	 * @param falseObj  falseObj
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 22:17:31
	 */
	public Object w2(String condition, Object data1, Object data2, Object trueObj,
			Object falseObj) {
		if ("==".equals(condition)) {
			if (data1 == data2) {
				return trueObj;
			}
			if (data1 != null && data1.equals(data2)) {
				return trueObj;
			}
			return falseObj;
		}
		if (">".equals(condition)) {
			if (((Number) data1).doubleValue() > ((Number) data2).doubleValue()) {
				return trueObj;
			} else {
				return falseObj;
			}
		}
		if ("<".equals(condition)) {
			if (((Number) data1).doubleValue() < ((Number) data2).doubleValue()) {
				return trueObj;
			} else {
				return falseObj;
			}
		}
		throw new RuntimeException("条件不符合规范");
	}

	/**
	 * enumDesc
	 *
	 * @param enumClass enumClass
	 * @param value     value
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 22:17:35
	 */
	public String enumDesc(String enumClass, Object value) {
		try {
			for (Object item : Class.forName(enumClass).getEnumConstants()) {
				if (item == value || item.getClass().getField("Value").get(item).toString()
						.equals(value.toString())) {
					return item.getClass().getField("Desc").get(item).toString();
				}
			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * enums
	 *
	 * @param enumClass enumClass
	 * @return {@link java.util.List }
	 * @since 2021-09-02 22:17:39
	 */
	public List<Object> enums(String enumClass) {
		try {
			List<Object> objs = new ArrayList<>();
			objs.addAll(Arrays.asList(Class.forName(enumClass).getEnumConstants()));
			return objs;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	/**
	 * filed
	 *
	 * @param o         o
	 * @param filedName filedName
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 22:17:43
	 */
	public Object filed(Object o, String filedName) {
		try {
			Field f = o.getClass().getField(filedName);
			f.setAccessible(true);
			return f.get(o);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * filed2
	 *
	 * @param o         o
	 * @param filedName filedName
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 22:17:47
	 */
	public Object filed2(Object o, String filedName) {
		try {
			String[] fs = filedName.split("\\.");
			Object d = o;
			for (String f : fs) {
				if (d != null) {
					Field v = d.getClass().getDeclaredField(f);
					v.setAccessible(true);
					d = v.get(d);
				} else {
					return "";
				}
			}
			return d;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * toJson
	 *
	 * @param o o
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 22:17:55
	 */
	public String toJson(Object o) {
		return JsonUtil.toJSONString(o);
	}
}
