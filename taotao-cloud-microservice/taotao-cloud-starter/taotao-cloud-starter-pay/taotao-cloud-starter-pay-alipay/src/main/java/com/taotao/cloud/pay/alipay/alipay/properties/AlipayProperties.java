/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.pay.alipay.alipay.properties;

import com.google.common.base.MoreObjects;
import com.taotao.cloud.pay.alipay.alipay.definition.AlipayProfile;
import com.taotao.cloud.pay.common.constants.PayConstants;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: 支付宝配置属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 18:10
 */
@ConfigurationProperties(prefix = PayConstants.PROPERTY_PAY_ALIPAY)
public class AlipayProperties {

	/**
	 * 是否开启支付宝使用
	 */
	private Boolean enabled;
	/**
	 * 是否是沙箱模式，默认值：false。
	 */
	private Boolean sandbox = false;
	/**
	 * 是否是证书模式，默认值：false。 false，公钥模式；true，证书模式；
	 */
	private Boolean certMode = false;
	/**
	 * 默认的 Profile 自定义唯一标识 Key
	 */
	private String defaultProfile;
	/**
	 * 支付宝支付信息配置，支持多个。以自定义唯一标识作为 Key。
	 */
	private Map<String, AlipayProfile> profiles;
	/**
	 * 用户确认支付后，支付宝通过 get 请求 returnUrl（商户入参传入），返回同步返回参数。
	 */
	private String returnUrl;
	/**
	 * 交易成功后，支付宝通过 post 请求 notifyUrl（商户入参传入），返回异步通知参数。
	 */
	private String notifyUrl;
	/**
	 * 支付通知处理服务的服务名
	 */
	private String destination;

	public Boolean getSandbox() {
		return sandbox;
	}

	public void setSandbox(Boolean sandbox) {
		this.sandbox = sandbox;
	}

	public Boolean getCertMode() {
		return certMode;
	}

	public void setCertMode(Boolean certMode) {
		this.certMode = certMode;
	}

	public String getDefaultProfile() {
		return defaultProfile;
	}

	public void setDefaultProfile(String defaultProfile) {
		this.defaultProfile = defaultProfile;
	}

	public Map<String, AlipayProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Map<String, AlipayProfile> profiles) {
		this.profiles = profiles;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("enabled", enabled)
			.add("sandbox", sandbox)
			.add("certMode", certMode)
			.add("defaultProfile", defaultProfile)
			.add("returnUrl", returnUrl)
			.add("notifyUrl", notifyUrl)
			.add("destination", destination)
			.toString();
	}
}
