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
package com.taotao.cloud.sms.channel.qcloud;

import com.taotao.cloud.sms.model.AbstractHandlerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 腾讯云短信配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:51:42
 */
@ConfigurationProperties(prefix = QCloudProperties.PREFIX)
public class QCloudProperties extends AbstractHandlerProperties<Integer> {
	public static final String PREFIX = "taotao.cloud.sms.qcloud";
	private boolean enabled = false;
    /**
     * 短信应用SDK AppID
     */
    private int appId;

    /**
     * 短信应用SDK AppKey
     */
    private String appkey;

    /**
     * 短信签名
     */
    private String smsSign;


	public int getAppId() {
		return appId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getSmsSign() {
		return smsSign;
	}

	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}
}
