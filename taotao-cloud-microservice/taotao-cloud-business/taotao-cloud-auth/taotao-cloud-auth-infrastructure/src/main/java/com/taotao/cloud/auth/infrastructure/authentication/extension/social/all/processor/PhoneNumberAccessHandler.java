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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.processor;


import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.definition.AccessHandler;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.definition.AccessResponse;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.definition.AccessUserDetails;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.exception.AccessIdentityVerificationFailedException;
import com.taotao.boot.security.spring.core.AccessPrincipal;

/**
 * <p>手机短信接入处理器 </p>
 *
 * @since : 2022/1/26 11:46
 */
public class PhoneNumberAccessHandler implements AccessHandler {

	//	private final VerificationCodeStampManager verificationCodeStampManager;
	//	private final SmsSendStrategyFactory smsSendStrategyFactory;
	//
	//	public PhoneNumberAccessHandler(VerificationCodeStampManager verificationCodeStampManager, SmsSendStrategyFactory
	// smsSendStrategyFactory) {
	//		this.verificationCodeStampManager = verificationCodeStampManager;
	//		this.smsSendStrategyFactory = smsSendStrategyFactory;
	//	}

	@Override
	public AccessResponse preProcess(String core, String... params) {
		//		String code = verificationCodeStampManager.create(core);
		//		boolean result;
		//		if (verificationCodeStampManager.getSandbox()) {
		//			result = true;
		//		} else {
		//			Template template = new Template();
		//			template.setType(verificationCodeStampManager.getVerificationCodeTemplateId());
		//			template.setParams(ImmutableMap.of(BaseConstants.CODE, code));
		//			result = smsSendStrategyFactory.send(template, core);
		//		}

		AccessResponse accessResponse = new AccessResponse();
		accessResponse.setSuccess(true);
		return accessResponse;
	}

	@Override
	public AccessUserDetails loadUserDetails(String source, AccessPrincipal accessPrincipal) {
		//		boolean isCodeOk = verificationCodeStampManager.check(accessPrincipal.getMobile(),
		// accessPrincipal.getCode());
		//		if (isCodeOk) {
		//			AccessUserDetails accessUserDetails = new AccessUserDetails();
		//			accessUserDetails.setUuid(accessPrincipal.getMobile());
		//			accessUserDetails.setPhoneNumber(accessPrincipal.getMobile());
		//			accessUserDetails.setUserName(accessPrincipal.getMobile());
		//			accessUserDetails.setSource(source);
		//
		//			verificationCodeStampManager.delete(accessPrincipal.getMobile());
		//			return accessUserDetails;
		//		}

		throw new AccessIdentityVerificationFailedException("Phone Verification Code Error!");
	}
}
