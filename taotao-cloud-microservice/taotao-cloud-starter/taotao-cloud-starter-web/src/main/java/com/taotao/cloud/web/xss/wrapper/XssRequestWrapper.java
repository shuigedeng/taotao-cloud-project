package com.taotao.cloud.web.xss.wrapper;


import static com.taotao.cloud.web.xss.utils.XssUtils.xssClean;

import com.taotao.cloud.common.utils.LogUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * 跨站攻击请求包装器
 *
 * @author zuihou
 * @date 2019-06-28 17:04
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
		try {
			if (null != queryString) {
				queryString = URLDecoder.decode(queryString, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			LogUtil.error("getQueryString", e);
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
