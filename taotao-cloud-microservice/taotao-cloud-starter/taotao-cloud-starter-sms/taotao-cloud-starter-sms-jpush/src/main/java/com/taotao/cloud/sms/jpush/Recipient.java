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
package com.taotao.cloud.sms.jpush;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * 接收者
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:51:15
 */
public class Recipient {

    /**
     * 手机号码
     */
    private String mobile;

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
     * 模板参数
     */
    @JsonProperty("temp_para")
    private Map<String, String> tempPara;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

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

	public Map<String, String> getTempPara() {
		return tempPara;
	}

	public void setTempPara(Map<String, String> tempPara) {
		this.tempPara = tempPara;
	}
}
