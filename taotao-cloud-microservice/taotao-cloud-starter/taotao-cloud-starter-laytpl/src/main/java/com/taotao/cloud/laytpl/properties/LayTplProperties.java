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
package com.taotao.cloud.laytpl.properties;

import com.taotao.cloud.common.utils.date.DateUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * LayTpl配置
 */
@RefreshScope
@ConfigurationProperties(prefix = LayTplProperties.PREFIX)
public class LayTplProperties {

	public static final String PREFIX = "taotao.cloud.laytpl";

	private boolean enabled = false;

	/**
	 * 模板分隔符开始，默认：{{
	 */
	private String open = "{{";
	/**
	 * 模板分隔符结束，默认：}}
	 */
	private String close = "}}";
	/**
	 * 模板前缀，默认：classpath:templates/tpl/
	 */
	private String prefix = "classpath:templates/tpl/";
	/**
	 * 缓存模板，默认：true
	 */
	private boolean cache = true;
	/**
	 * 数字格式化，默认：#.00
	 */
	private String numPattern = "#.00";
	/**
	 * Date 日期格式化，默认："yyyy-MM-dd HH:mm:ss"
	 */
	private String datePattern = DateUtil.PATTERN_DATETIME;
	/**
	 * java8 LocalTime时间格式化，默认："HH:mm:ss"
	 */
	private String localTimePattern = DateUtil.PATTERN_TIME;
	/**
	 * java8 LocalDate日期格式化，默认："yyyy-MM-dd"
	 */
	private String localDatePattern = DateUtil.PATTERN_DATE;
	/**
	 * java8 LocalDateTime日期时间格式化，默认："yyyy-MM-dd HH:mm:ss"
	 */
	private String localDateTimePattern = DateUtil.PATTERN_DATETIME;

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}

	public String getNumPattern() {
		return numPattern;
	}

	public void setNumPattern(String numPattern) {
		this.numPattern = numPattern;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getLocalTimePattern() {
		return localTimePattern;
	}

	public void setLocalTimePattern(String localTimePattern) {
		this.localTimePattern = localTimePattern;
	}

	public String getLocalDatePattern() {
		return localDatePattern;
	}

	public void setLocalDatePattern(String localDatePattern) {
		this.localDatePattern = localDatePattern;
	}

	public String getLocalDateTimePattern() {
		return localDateTimePattern;
	}

	public void setLocalDateTimePattern(String localDateTimePattern) {
		this.localDateTimePattern = localDateTimePattern;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
