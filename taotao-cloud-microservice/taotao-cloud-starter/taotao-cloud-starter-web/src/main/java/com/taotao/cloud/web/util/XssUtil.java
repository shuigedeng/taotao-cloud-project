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
package com.taotao.cloud.web.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.LogUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

/**
 * XSS 工具类， 用于过滤特殊字符
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:29:42
 */
public class XssUtil {

	private static final String ANTISAMY_SLASHDOT_XML = "antisamy-slashdot-1.4.4.xml";
	private static Policy policy = null;
	private static final Pattern SCRIPT_BETWEEN_PATTERN = Pattern.compile(
			"<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern SCRIPT_END_PATTERN = Pattern.compile(
			"</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
	private static final Pattern SCRIPT_START_PATTERN = Pattern.compile("<[\r\n| | ]*script(.*?)>",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern EVAL_PATTERN = Pattern.compile("eval\\((.*?)\\)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern E_XPRESSION_PATTERN = Pattern.compile("e-xpression\\((.*?)\\)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern MOCHA_PATTERN = Pattern.compile("mocha[\r\n| | ]*:[\r\n| | ]*",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern EXPRESSION_PATTERN = Pattern.compile("expression\\((.*?)\\)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern URL_PATTERN = Pattern.compile("url\\((.*?)\\)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern VBSCRIPT_PATTERN = Pattern.compile(
			"vbscript[\r\n| | ]*:[\r\n| | ]*",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern JAVASCRIPT_PATTERN = Pattern.compile(
			"javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
	private static final Pattern ONLOAD_PATTERN = Pattern.compile("onload(.*?)=",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern ONMOUSEOVER_PATTERN = Pattern.compile("onMouseOver=.*?//",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern ONMOUSEOVER_PATTERN_2 = Pattern.compile("onmouseover(.*)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern ONMOUSEOVER_PATTERN_3 = Pattern.compile("onmouseover=.*?",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern ALERT_PATTERN = Pattern.compile("alert(.*)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	private static String REPLACE_STRING = "";
	private static Pattern script = null;

	static {
		script = Pattern.compile(
				"<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>",
				Pattern.CASE_INSENSITIVE);

		LogUtil.debug(" start read XSS config file [" + ANTISAMY_SLASHDOT_XML + "]");
		InputStream inputStream = XssUtil.class.getClassLoader()
				.getResourceAsStream(ANTISAMY_SLASHDOT_XML);
		try {
			policy = Policy.getInstance(inputStream);
			LogUtil.debug("read XSS config file [" + ANTISAMY_SLASHDOT_XML + "] success");
		} catch (PolicyException e) {
			LogUtil.error("read XSS config file [" + ANTISAMY_SLASHDOT_XML + "] fail , reason:", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LogUtil.error(
							"close XSS config file [" + ANTISAMY_SLASHDOT_XML + "] fail , reason:",
							e);
				}
			}
		}
	}

	/**
	 * 跨站攻击语句过滤
	 *
	 * @param paramValue           待过滤的参数
	 * @param ignoreParamValueList 忽略过滤的参数列表
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 22:29:53
	 */
	public static String xssClean(String paramValue, List<String> ignoreParamValueList) {
		AntiSamy antiSamy = new AntiSamy();

		try {
			LogUtil.debug("raw value before xssClean: " + paramValue);
			if (isIgnoreParamValue(paramValue, ignoreParamValueList)) {
				LogUtil.debug("ignore the xssClean,keep the raw paramValue: " + paramValue);
				return paramValue;
			} else {
				final CleanResults cr = antiSamy.scan(paramValue, policy);
				cr.getErrorMessages().forEach(LogUtil::debug);
				String str = cr.getCleanHTML();
				str = stripXSSAndSql(str);
				str = str.replaceAll("&quot;", "\"");
				str = str.replaceAll("&amp;", "&");
				//str = str.replaceAll("'", "'");
				//str = str.replaceAll("'", "＇");
				//str = str.replaceAll(" ", "*");
				str = str.replaceAll("&lt;", "<");
				str = str.replaceAll("&gt;", ">");
				LogUtil.debug("xss filter value after xssClean" + str);

				return str;
			}
		} catch (ScanException e) {
			LogUtil.error("scan failed is [" + paramValue + "]", e);
		} catch (PolicyException e) {
			LogUtil.error("antisamy convert failed  is [" + paramValue + "]", e);
		}
		return paramValue;
	}

	/**
	 * 过滤形参
	 *
	 * @param paramValue           paramValue
	 * @param ignoreParamValueList ignoreParamValueList
	 * @param param                param
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 22:30:03
	 */
	public static String xssClean(String paramValue, List<String> ignoreParamValueList,
			String param) {
		if (isIgnoreParamValue(param, ignoreParamValueList)) {
			//虽然过滤固定字段 允许标签 但是关键函数必须处理 不允许出现
			return stripXSSAndSql(paramValue);
		} else {
			return xssClean(paramValue, ignoreParamValueList);
		}
	}

	/**
	 * xss校验
	 *
	 * @param value value
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 22:30:13
	 */
	public static String stripXSSAndSql(String value) {
		if (StrUtil.isBlank(value)) {
			return value;
		}
		// Avoid anything between script tags
		value = SCRIPT_BETWEEN_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		// Remove any lonesome </script> tag
		value = SCRIPT_END_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		// Remove any lonesome <script ...> tag
		value = SCRIPT_START_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		// Avoid eval(...) expressions
		value = EVAL_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		// Avoid e-xpression(...) expressions
		value = E_XPRESSION_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		value = MOCHA_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		value = EXPRESSION_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		value = URL_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		// Avoid vbscript:... expressions
		value = VBSCRIPT_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		// Avoid javascript:... expressions
		value = JAVASCRIPT_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		// Avoid onload= expressions
		value = ONLOAD_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		// Avoid onMouseOver= expressions
		value = ONMOUSEOVER_PATTERN.matcher(value).replaceAll(REPLACE_STRING);
		value = ONMOUSEOVER_PATTERN_2.matcher(value).replaceAll(REPLACE_STRING);
		value = ONMOUSEOVER_PATTERN_3.matcher(value).replaceAll(REPLACE_STRING);
		value = ALERT_PATTERN.matcher(value).replaceAll(REPLACE_STRING);

		return value;
	}

	/**
	 * isIgnoreParamValue
	 *
	 * @param paramValue           paramValue
	 * @param ignoreParamValueList ignoreParamValueList
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 22:30:21
	 */
	private static boolean isIgnoreParamValue(String paramValue,
			List<String> ignoreParamValueList) {
		if (StrUtil.isBlank(paramValue)) {
			return true;
		}
		if (CollectionUtil.isEmpty(ignoreParamValueList)) {
			return false;
		}
		for (String ignoreParamValue : ignoreParamValueList) {
			if (paramValue.contains(ignoreParamValue)) {
				return true;
			}
		}

		return false;
	}

	//public static void main(String[] args) {
	//    System.out.println(stripXSSAndSql("<span style=\"background&#45;color&#58; &#35;e67e23\">减肥啦</span>"));
	//    try {
	//        String url = URLDecoder.decode("<img src=\"http://112.30.98.118:9025/file/1/image/public/202012/20201204160811_mxsroqyxdk.jpg\">", "UTF-8");
	//        System.out.println(url);
	//    } catch (UnsupportedEncodingException e) {
	//        e.printStackTrace();
	//    }
	//}
}
