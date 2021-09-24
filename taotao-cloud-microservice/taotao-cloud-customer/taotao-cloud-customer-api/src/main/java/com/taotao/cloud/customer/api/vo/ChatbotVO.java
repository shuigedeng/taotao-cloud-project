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
package com.taotao.cloud.customer.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/20 上午9:42
 */
@Schema(name = "机器人客服VO", description = "机器人客服VO")
public class ChatbotVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "机器人名称")
	private String name;

	@Schema(description = "基础url")
	private String baseUrl;

	@Schema(description = "首选语言")
	private String primaryLanguage;

	@Schema(description = "兜底回复")
	private String fallback;

	@Schema(description = "欢迎语")
	private String welcome;

	@Schema(description = "渠道类型")
	private String channel;

	@Schema(description = "渠道标识")
	private String channelMark;

	@Schema(description = "是否开启 0-未开启，1-开启")
	private Boolean enabled;

	@Schema(description = "工作模式")
	private Integer workMode;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getFallback() {
		return fallback;
	}

	public void setFallback(String fallback) {
		this.fallback = fallback;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelMark() {
		return channelMark;
	}

	public void setChannelMark(String channelMark) {
		this.channelMark = channelMark;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getWorkMode() {
		return workMode;
	}

	public void setWorkMode(Integer workMode) {
		this.workMode = workMode;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}
