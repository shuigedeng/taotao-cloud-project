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
package com.taotao.cloud.xss.support;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import static com.taotao.cloud.xss.utils.XssUtil.xssClean;

/**
 * 跨站攻击请求包装器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 08:05:44
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

	private final List<String> ignoreParamValueList;

	public XssRequestWrapper(HttpServletRequest request, List<String> ignoreParamValueList) {
		super(request);
		this.ignoreParamValueList = ignoreParamValueList;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> requestMap = super.getParameterMap();
		for (Map.Entry<String, String[]> me : requestMap.entrySet()) {
			String[] values = me.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = xssClean(values[i], this.ignoreParamValueList);
			}
		}
		return requestMap;
	}

	@Override
	public String getQueryString() {
		String queryString = super.getQueryString();
		if (null != queryString) {
			queryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
		}
		return xssClean(queryString, this.ignoreParamValueList);
	}

	@Override
	public String[] getParameterValues(String paramString) {
		String[] arrayOfString1 = super.getParameterValues(paramString);
		if (arrayOfString1 == null) {
			return null;
		}
		int i = arrayOfString1.length;
		String[] arrayOfString2 = new String[i];
		for (int j = 0; j < i; j++) {
			arrayOfString2[j] = xssClean(arrayOfString1[j], this.ignoreParamValueList, paramString);
		}
		return arrayOfString2;
	}

	@Override
	public String getParameter(String paramString) {
		String str = super.getParameter(paramString);
		if (str == null) {
			return null;
		}
		return xssClean(str, this.ignoreParamValueList);
	}

	@Override
	public String getHeader(String paramString) {
		String str = super.getHeader(paramString);
		if (str == null) {
			return null;
		}
		return xssClean(str, this.ignoreParamValueList);
	}
}
