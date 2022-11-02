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

package com.taotao.cloud.pay.wxpay.wechat.configuration;

import com.taotao.cloud.pay.wxpay.wechat.annotation.ConditionalOnWxpay;
import com.taotao.cloud.pay.wxpay.wechat.definition.WxpayPaymentTemplate;
import com.taotao.cloud.pay.wxpay.wechat.definition.WxpayProfileStorage;
import com.taotao.cloud.pay.wxpay.wechat.properties.WxpayProperties;
import com.taotao.cloud.pay.wxpay.wechat.support.WxpayDefaultProfileStorage;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 微信支付 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/11 12:34
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWxpay
@EnableConfigurationProperties(WxpayProperties.class)
public class WxpayConfiguration {

	private static final Logger log = LoggerFactory.getLogger(WxpayConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("[Herodotus] |- SDK [Engine Pay Wxpay] Auto Configure.");
	}

	@Bean
	@ConditionalOnMissingBean
	public WxpayProfileStorage wxpayDefaultProfileStorage(WxpayProperties wxpayProperties) {
		WxpayDefaultProfileStorage wxpayDefaultProfileStorage = new WxpayDefaultProfileStorage(
			wxpayProperties);
		log.debug("[Herodotus] |- Bean [Wxpay Default Profile Storage] Auto Configure.");
		return wxpayDefaultProfileStorage;
	}

	@Bean
	@ConditionalOnMissingBean
	public WxpayPaymentTemplate wxpayPaymentTemplate(WxpayProfileStorage wxpayProfileStorage,
		WxpayProperties wxpayProperties) {
		WxpayPaymentTemplate wxpayPaymentTemplate = new WxpayPaymentTemplate(wxpayProfileStorage,
			wxpayProperties);
		log.trace("[Herodotus] |- Bean [Wxpay Payment Template] Auto Configure.");
		return wxpayPaymentTemplate;
	}
}
