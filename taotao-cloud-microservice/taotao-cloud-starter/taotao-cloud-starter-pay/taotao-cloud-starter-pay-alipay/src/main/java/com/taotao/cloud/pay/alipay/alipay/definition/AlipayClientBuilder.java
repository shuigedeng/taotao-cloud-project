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

package com.taotao.cloud.pay.alipay.alipay.definition;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.taotao.cloud.pay.common.exception.PaymentClientUninitializedException;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: AlipayClient 构造器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/8 15:18
 */
public class AlipayClientBuilder {

	private static final Logger log = LoggerFactory.getLogger(AlipayClientBuilder.class);

	private static final String FORMAT = "json";

	private static volatile AlipayClientBuilder instance;

	/**
	 * 支付宝相关属性
	 */
	private String appId;
	private String appPrivateKey;
	private String charset;
	private String alipayPublicKey;
	private String signType;
	private String appCertPath;
	private String alipayCertPath;
	private String alipayRootCertPath;
	private String appCertContent;
	private String alipayCertContent;
	private String alipayRootCertContent;

	/**
	 * 自定义属性
	 */
	private Boolean sandbox;
	private Boolean certMode;
	private Boolean contentMode;

	private AlipayClientBuilder(Boolean sandbox, Boolean certMode, Boolean contentMode) {
		this.sandbox = sandbox;
		this.certMode = certMode;
		this.contentMode = contentMode;
	}

	public static AlipayClientBuilder mode(Boolean sandbox, Boolean certMode) {
		return mode(sandbox, certMode, false);
	}

	public static AlipayClientBuilder mode(Boolean sandbox, Boolean certMode, Boolean contentMode) {
		if (ObjectUtils.isEmpty(instance)) {
			synchronized (AlipayClientBuilder.class) {
				if (ObjectUtils.isEmpty(instance)) {
					instance = new AlipayClientBuilder(sandbox, certMode, contentMode);
				}
			}
		}
		instance.setSandbox(sandbox);
		instance.setCertMode(certMode);
		instance.setContentMode(contentMode);
		return instance;
	}

	public AlipayClient build() {
		if (getCertMode()) {
			if (getContentMode()) {
				return createByContent(getSandbox());
			} else {
				return createByPath(getSandbox());
			}
		} else {
			return create(getSandbox());
		}
	}

	/**
	 * 普通公钥方式
	 *
	 * @return AliPayApiConfig 支付宝配置
	 */
	private AlipayClient create(boolean sandbox) {
		// return new DefaultAlipayClient(Sandbox.getAliPayServerUrl(sandbox), getAppId(),
		// 	getAppPrivateKey(), FORMAT, getCharset(), getAlipayPublicKey(), getSignType());
		return null;
	}

	private AlipayClient createByPath(boolean sandbox) {
		CertAlipayRequest certAlipayRequest = createCommon(sandbox, getAppId(), getAppPrivateKey(),
			getCharset(), getSignType());
		certAlipayRequest.setCertPath(getAppCertPath());
		certAlipayRequest.setAlipayPublicCertPath(getAlipayCertPath());
		certAlipayRequest.setRootCertPath(getAlipayRootCertPath());
		try {
			return new DefaultAlipayClient(certAlipayRequest);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay client uninitialized, by path!");
			throw new PaymentClientUninitializedException("Alipay client uninitialized.",
				e.getCause());
		}
	}

	private AlipayClient createByContent(boolean sandbox) {
		CertAlipayRequest certAlipayRequest = createCommon(sandbox, getAppId(), getAppPrivateKey(),
			getCharset(), getSignType());
		certAlipayRequest.setCertContent(getAppCertContent());
		certAlipayRequest.setAlipayPublicCertContent(getAlipayCertContent());
		certAlipayRequest.setRootCertContent(getAlipayRootCertContent());
		try {
			return new DefaultAlipayClient(certAlipayRequest);
		} catch (AlipayApiException e) {
			log.error("[Herodotus] |- Alipay client uninitialized by content!");
			throw new PaymentClientUninitializedException("Alipay client uninitialized.",
				e.getCause());
		}
	}

	@NotNull
	private CertAlipayRequest createCommon(boolean sandbox, String appId, String appPrivateKey,
										   String charset, String signType) {
		CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
		// certAlipayRequest.setServerUrl(Sandbox.getAliPayServerUrl(sandbox));
		certAlipayRequest.setAppId(appId);
		certAlipayRequest.setPrivateKey(appPrivateKey);
		certAlipayRequest.setFormat(FORMAT);
		certAlipayRequest.setCharset(charset);
		certAlipayRequest.setSignType(signType);
		return certAlipayRequest;
	}

	private String getAppId() {
		return appId;
	}

	public AlipayClientBuilder setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	private String getAppPrivateKey() {
		return appPrivateKey;
	}

	public AlipayClientBuilder setAppPrivateKey(String appPrivateKey) {
		this.appPrivateKey = appPrivateKey;
		return this;
	}

	private String getCharset() {
		return charset;
	}

	public AlipayClientBuilder setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	private String getAlipayPublicKey() {
		return alipayPublicKey;
	}

	public AlipayClientBuilder setAlipayPublicKey(String alipayPublicKey) {
		this.alipayPublicKey = alipayPublicKey;
		return this;
	}

	private String getSignType() {
		return signType;
	}

	public AlipayClientBuilder setSignType(String signType) {
		this.signType = signType;
		return this;
	}

	private String getAppCertPath() {
		return appCertPath;
	}

	public AlipayClientBuilder setAppCertPath(String appCertPath) {
		this.appCertPath = appCertPath;
		return this;
	}

	private String getAlipayCertPath() {
		return alipayCertPath;
	}

	public AlipayClientBuilder setAlipayCertPath(String alipayCertPath) {
		this.alipayCertPath = alipayCertPath;
		return this;
	}

	private String getAlipayRootCertPath() {
		return alipayRootCertPath;
	}

	public AlipayClientBuilder setAlipayRootCertPath(String alipayRootCertPath) {
		this.alipayRootCertPath = alipayRootCertPath;
		return this;
	}

	private String getAppCertContent() {
		return appCertContent;
	}

	public AlipayClientBuilder setAppCertContent(String appCertContent) {
		this.appCertContent = appCertContent;
		return this;
	}

	private String getAlipayCertContent() {
		return alipayCertContent;
	}

	public AlipayClientBuilder setAlipayCertContent(String alipayCertContent) {
		this.alipayCertContent = alipayCertContent;
		return this;
	}

	private String getAlipayRootCertContent() {
		return alipayRootCertContent;
	}

	public AlipayClientBuilder setAlipayRootCertContent(String alipayRootCertContent) {
		this.alipayRootCertContent = alipayRootCertContent;
		return this;
	}

	private Boolean getSandbox() {
		return sandbox;
	}

	private void setSandbox(Boolean sandbox) {
		this.sandbox = sandbox;
	}

	private Boolean getCertMode() {
		return certMode;
	}

	private void setCertMode(Boolean certMode) {
		this.certMode = certMode;
	}

	private Boolean getContentMode() {
		return contentMode;
	}

	private void setContentMode(Boolean contentMode) {
		this.contentMode = contentMode;
	}
}
