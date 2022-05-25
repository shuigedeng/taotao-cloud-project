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
package com.taotao.cloud.sms.web;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sms.model.NoticeInfo;
import com.taotao.cloud.sms.model.VerifyInfo;
import com.taotao.cloud.sms.utils.StringUtils;
import java.lang.reflect.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 短信控制器路径自动配置
 *
 * @author shuigedeng
 */
@AutoConfiguration(after = SmsWebmvcAutoConfiguration.class)
@ConditionalOnProperty(prefix = SmsWebmvcProperties.PREFIX, name = "enable", havingValue = "true")
public class SmsWebmvcPathAutoConfiguration {

	private static String getBasePath(SmsWebmvcProperties properties) {
		String bathPath = StringUtils.trimToNull(properties.getBasePath());

		return bathPath == null ? SmsWebmvcProperties.DEFAULT_BASE_PATH : bathPath;
	}

	/**
	 * 构造短信Controller
	 *
	 * @param controller 短信Controller
	 * @param mapping    RequestMappingHandlerMapping
	 * @param properties 短信Web配置
	 * @throws NoSuchMethodException if a matching method is not found or if the name is
	 *                               "&lt;init&gt;"or "&lt;clinit&gt;".
	 * @throws SecurityException     If a security manager, <i>s</i>, is present and the caller's
	 *                               class loader is not the same as or an ancestor of the class
	 *                               loader for the current class and invocation of {@link
	 *                               SecurityManager#checkPackageAccess s.checkPackageAccess()}
	 *                               denies access to the package of this class.
	 */
	@Autowired
	@ConditionalOnBean(RequestMappingHandlerMapping.class)
	public void smsController(SmsWebmvcProperties properties,
		RequestMappingHandlerMapping mapping,
		SmsController controller)
		throws NoSuchMethodException, SecurityException {

		String bathPath = getBasePath(properties);

		if (properties.isEnableSend()) {
			Method sendMethod = SmsController.class.getMethod("sendVerificationCode", String.class);
			RequestMappingInfo sendInfo = RequestMappingInfo.paths(
					bathPath + "/verificationCode/{phone}")
				.methods(RequestMethod.POST).build();
			mapping.registerMapping(sendInfo, controller, sendMethod);
			LogUtil.debug("registerMapping: {}", sendInfo);
		} else {
			LogUtil.debug("not register: sendInfo");
		}

		if (properties.isEnableGet()) {
			Method getMethod = SmsController.class.getMethod("getVerificationCode", String.class,
				String.class);
			RequestMappingInfo getInfo = RequestMappingInfo.paths(
					bathPath + "/verificationCode/{phone}")
				.methods(RequestMethod.GET).produces("application/json").build();
			mapping.registerMapping(getInfo, controller, getMethod);
			LogUtil.debug("registerMapping: {}", getInfo);
		} else {
			LogUtil.debug("not register: getInfo");
		}

		if (properties.isEnableVerify()) {
			Method verifyMethod = SmsController.class.getMethod("verifyVerificationCode",
				VerifyInfo.class);
			RequestMappingInfo verifyInfo = RequestMappingInfo.paths(bathPath + "/verificationCode")
				.methods(RequestMethod.POST).build();
			mapping.registerMapping(verifyInfo, controller, verifyMethod);
			LogUtil.debug("registerMapping: {}", verifyInfo);
		} else {
			LogUtil.debug("not register: verifyInfo");
		}

		if (properties.isEnableNotice()) {
			Method noticeMethod = SmsController.class.getMethod("sendNotice", NoticeInfo.class);
			RequestMappingInfo noticeInfo = RequestMappingInfo.paths(bathPath + "/notice")
				.methods(RequestMethod.PUT)
				.build();
			mapping.registerMapping(noticeInfo, controller, noticeMethod);
			LogUtil.debug("registerMapping: {}", noticeInfo);
		} else {
			LogUtil.debug("not register: noticeInfo");
		}
	}
}
