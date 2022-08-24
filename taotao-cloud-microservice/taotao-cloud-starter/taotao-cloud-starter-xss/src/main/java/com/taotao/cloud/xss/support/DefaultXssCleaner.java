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

import com.taotao.cloud.common.utils.lang.StringUtil;
import java.nio.charset.StandardCharsets;

import com.taotao.cloud.xss.properties.XssProperties;
import com.taotao.cloud.xss.utils.XssUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Cleaner;
import org.springframework.web.util.HtmlUtils;

/**
 * 默认的 xss 清理器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
public class DefaultXssCleaner implements XssCleaner {

	private final XssProperties properties;

	public DefaultXssCleaner(XssProperties properties) {
		this.properties = properties;
	}

	@Override
	public String clean(String bodyHtml) {
		// 1. 为空直接返回
		if (StringUtil.isBlank(bodyHtml)) {
			return bodyHtml;
		}

		XssProperties.Mode mode = properties.getMode();
		if (XssProperties.Mode.escape == mode) {
			// html 转义
			return HtmlUtils.htmlEscape(bodyHtml, StandardCharsets.UTF_8.name());
		} else {
			// jsoup html 清理
			Document.OutputSettings outputSettings = new Document.OutputSettings()
				// 2. 转义，没找到关闭的方法，目前这个规则最少
				.escapeMode(Entities.EscapeMode.xhtml)
				// 3. 保留换行
				.prettyPrint(properties.getPrettyPrint());

			Document dirty = Jsoup.parseBodyFragment(bodyHtml, "");
			Cleaner cleaner = new Cleaner(XssUtil.WHITE_LIST);
			Document clean = cleaner.clean(dirty);
			clean.outputSettings(outputSettings);

			// 4. 清理后的 html
			String escapedHtml = clean.body().html();
			if (properties.getEnableEscape()) {
				return escapedHtml;
			}

			// 5. 反转义
			return Entities.unescape(escapedHtml);
		}
	}

}
