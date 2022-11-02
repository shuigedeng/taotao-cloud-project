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

package com.taotao.cloud.pay.wxpay.wechat.definition;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.taotao.cloud.pay.common.exception.PaymentProfileIdIncorrectException;
import com.taotao.cloud.pay.common.exception.PaymentProfileNotFoundException;
import com.taotao.cloud.pay.wxpay.wechat.properties.WxpayProperties;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 微信支付模版 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/11 12:10
 */
public class WxpayPaymentTemplate {

	private static final Logger log = LoggerFactory.getLogger(WxpayPaymentTemplate.class);

	private final WxpayProfileStorage wxpayProfileStorage;
	private final WxpayProperties wxpayProperties;

	public WxpayPaymentTemplate(WxpayProfileStorage wxpayProfileStorage,
		WxpayProperties wxpayProperties) {
		this.wxpayProfileStorage = wxpayProfileStorage;
		this.wxpayProperties = wxpayProperties;
	}

	private WxpayProfileStorage getWxpayProfileStorage() {
		return wxpayProfileStorage;
	}

	private WxpayProperties getWxpayProperties() {
		return wxpayProperties;
	}

	private WxpayProfile getProfile(String identity) {
		WxpayProfile wxpayProfile = getWxpayProfileStorage().getProfile(identity);
		if (ObjectUtils.isNotEmpty(wxpayProfile)) {
			return wxpayProfile;
		} else {
			throw new PaymentProfileNotFoundException(
				"Payment profile for " + identity + " not found.");
		}
	}

	private WxpayPaymentExecuter getProcessor(Boolean sandbox, WxpayProfile wxpayProfile) {

		WxPayConfig payConfig = new WxPayConfig();
		payConfig.setAppId(wxpayProfile.getAppId());
		payConfig.setMchId(wxpayProfile.getMchId());
		payConfig.setMchKey(wxpayProfile.getMchKey());
		payConfig.setSubAppId(wxpayProfile.getSubAppId());
		payConfig.setSubMchId(wxpayProfile.getSubMchId());
		payConfig.setKeyPath(wxpayProfile.getKeyPath());

		// 可以指定是否使用沙箱环境
		payConfig.setUseSandboxEnv(sandbox);

		WxPayService wxPayService = new WxPayServiceImpl();
		wxPayService.setConfig(payConfig);
		return new WxpayPaymentExecuter(wxPayService);
	}

	public WxpayPaymentExecuter getProcessor(String identity) {

		String id =
			StringUtils.isNotBlank(identity) ? identity : getWxpayProperties().getDefaultProfile();

		if (StringUtils.isBlank(id)) {
			throw new PaymentProfileIdIncorrectException(
				"Payment profile incorrect, or try to set default profile id.");
		}

		WxpayProfile wxpayProfile = getProfile(identity);
		return getProcessor(getWxpayProperties().getSandbox(), wxpayProfile);
	}

}
