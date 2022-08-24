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
package com.taotao.cloud.xss.filter;

import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.lang3.ObjectUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.util.ResourceUtils;

/**
 * Antisamy 单例 工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 09:01:24
 */
public class XssUtils {

	private static volatile XssUtils INSTANCE;
	private final AntiSamy antiSamy;
	private final String nbsp;
	private final String quot;

	private XssUtils() {
		Policy policy = createPolicy();
		this.antiSamy = ObjectUtils.isNotEmpty(policy) ? new AntiSamy(policy) : new AntiSamy();
		this.nbsp = cleanHtml("&nbsp;");
		this.quot = cleanHtml("\"");
	}

	private static XssUtils getInstance() {
		if (ObjectUtils.isEmpty(INSTANCE)) {
			synchronized (XssUtils.class) {
				if (ObjectUtils.isEmpty(INSTANCE)) {
					INSTANCE = new XssUtils();
				}
			}
		}

		return INSTANCE;
	}

	private Policy createPolicy() {
		try {
			URL url = ResourceUtils.getURL("classpath:antisamy/antisamy-anythinggoes.xml");
			return Policy.getInstance(url);
		} catch (IOException | PolicyException e) {
			LogUtil.trace("Antisamy create policy error! {}", e.getMessage());
			return null;
		}
	}

	private CleanResults scan(String taintedHtml) throws ScanException, PolicyException {
		return antiSamy.scan(taintedHtml);
	}

	private String cleanHtml(String taintedHtml) {
		try {
			LogUtil.trace("Before Antisamy Scan, value is: [{}]", taintedHtml);
			// 使用AntiSamy清洗数据
			final CleanResults cleanResults = scan(taintedHtml);
			String result = cleanResults.getCleanHTML();
			LogUtil.trace("After  Antisamy Scan, value is: [{}]", result);
			return result;
		} catch (ScanException | PolicyException e) {
			LogUtil.error("Antisamy scan catch error! {}", e.getMessage());
			return taintedHtml;
		}
	}

	public static String cleaning(String taintedHTML) {
		// 对转义的HTML特殊字符（<、>、"等）进行反转义，因为AntiSamy调用scan方法时会将特殊字符转义
		String cleanHtml = StringUtil.escapeHtml(getInstance().cleanHtml(taintedHTML));
		//AntiSamy会把“&nbsp;”转换成乱码，把双引号转换成"&quot;" 先将&nbsp;的乱码替换为空，双引号的乱码替换为双引号
		String temp = cleanHtml.replaceAll(getInstance().nbsp, "");
		String result = temp.replaceAll(getInstance().quot, "\"");
		LogUtil.trace("After  Antisamy Well Formed, value is: [{}]", result);
		return result;
	}
}
