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

import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.util.StringUtils;

/**
 * 模板访问java代码的注入类 一般通过Html或者tpl访问 如:Html.request()或者tpl.request()
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:19:10
 */
public class TemplateProvider {

	/**
	 * request
	 *
	 * @return {@link javax.servlet.http.HttpServletRequest }
	 * @author shuigedeng
	 * @since 2021-09-02 22:19:15
	 */
	public HttpServletRequest request() {
		return RequestUtil.getRequest();
	}

	/**
	 * response
	 *
	 * @return {@link javax.servlet.http.HttpServletResponse }
	 * @author shuigedeng
	 * @since 2021-09-02 22:19:19
	 */
	public HttpServletResponse response() {
		return RequestUtil.getResponse();
	}

	/**
	 * session
	 *
	 * @return {@link javax.servlet.http.HttpSession }
	 * @author shuigedeng
	 * @since 2021-09-02 22:19:25
	 */
	public HttpSession session() {
		return RequestUtil.getRequest().getSession();
	}

	/**
	 * 设置参数 request.setAttribute(key,value)
	 *
	 * @param key   key
	 * @param value value
	 * @author shuigedeng
	 * @since 2021-09-02 22:19:35
	 */
	public void setattr(String key, Object value) {
		request().setAttribute(key, value);
	}

	/**
	 * 获取参数 默认request.getAttribute(key) 支持key,也支持public字段表达式多层数据获取（通过反射深度查找）；格式:
	 * key.字段(或public).字段(或public)
	 *
	 * @param key key
	 * @return {@link java.lang.Object }
	 * @author shuigedeng
	 * @since 2021-09-02 22:19:44
	 */
	public Object getattr(String key) {
		if (key.contains(".")) {
			Object r = null;
			String[] path = StringUtils.split(key, ".");
			if (path != null && path.length > 0) {
				for (int i = 0; i < path.length; i++) {
					String p = path[i];
					if (i == 0) {
						r = request().getAttribute(p);
					} else {
						try {
							Field f = r.getClass().getDeclaredField(p);
							f.setAccessible(true);
							r = f.get(r);
						} catch (Exception exp) {
							r = null;
						}
					}
					if (r == null) {
						break;
					}
				}
			}
			return r;
		} else {
			return request().getAttribute(key);
		}
	}

	/**
	 * where 三元运算符
	 *
	 * @param istrue   条件bool值
	 * @param trueObj  true 结果
	 * @param falseObj false结果
	 * @return {@link java.lang.Object }
	 * @author shuigedeng
	 * @since 2021-09-02 22:19:55
	 */
	public Object where(Boolean istrue, Object trueObj, Object falseObj) {
		return istrue ? trueObj : falseObj;
	}

	/**
	 * 截断字符串，后缀...
	 *
	 * @param str    字符串
	 * @param maxlen 最大长度
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 22:20:05
	 */
	public String cutString(String str, int maxlen) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		if (str.length() <= maxlen) {
			return str;
		}
		return StringUtil.subString3(str, maxlen);
	}

	/**
	 * 日期字符串
	 *
	 * @param date   date
	 * @param format format
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 22:20:14
	 */
	public String dateString(Date date, String format) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 默认打印
	 *
	 * @param o o
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 22:20:21
	 */
	public String print(Object o) {
		if (o == null) {
			return "";
		}
		if (o instanceof Date) {
			return dateString((Date) o, "yyyy-MM-dd HH:mm:ss");
		}
		return o.toString();
	}

}
