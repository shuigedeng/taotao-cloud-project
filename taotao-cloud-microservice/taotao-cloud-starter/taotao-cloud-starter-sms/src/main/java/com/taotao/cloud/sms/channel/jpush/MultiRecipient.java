/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.channel.jpush;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * 接收者列表
 *
 * @author shuigedeng
 */
public class MultiRecipient {

    /**
     * 签名ID
     */
    @JsonProperty("sign_id")
    private Integer signId;

    /**
     * 模板ID
     */
    @JsonProperty("temp_id")
    private Integer tempId;

    /**
     * 标签
     */
    private String tag;

    /**
     * 接收者列表
     */
    private Collection<Recipient> recipients;

	public Integer getSignId() {
		return signId;
	}

	public void setSignId(Integer signId) {
		this.signId = signId;
	}

	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Collection<Recipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(Collection<Recipient> recipients) {
		this.recipients = recipients;
	}
}
