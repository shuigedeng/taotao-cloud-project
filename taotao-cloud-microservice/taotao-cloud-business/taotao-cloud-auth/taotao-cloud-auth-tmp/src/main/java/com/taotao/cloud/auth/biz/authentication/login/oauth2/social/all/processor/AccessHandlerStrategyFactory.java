/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.processor;

import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.enums.AccountType;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessResponse;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.definition.AccessUserDetails;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.exception.AccessHandlerNotFoundException;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.exception.IllegalAccessArgumentException;
import com.taotao.cloud.security.springsecurity.core.definition.domain.AccessPrincipal;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: Access Handler 工厂 </p>
 * <p>
 * 通过该工厂模式，对接入的常规操作进行封装。避免导入引用各个组件，导致耦合性增大
 * <p>
 * 本处使用基于Spring Boot 的工厂模式
 * {@see :https://www.pianshen.com/article/466978086/}
 *
 * 
 * @date : 2021/4/4 17:40
 */
public class AccessHandlerStrategyFactory {

	private static final Logger log = LoggerFactory.getLogger(AccessHandlerStrategyFactory.class);

	@Autowired
	private final Map<String, AccessHandler> handlers = new ConcurrentHashMap<>();

	public AccessResponse preProcess(String source, String core, String... params) {
		AccessHandler socialAuthenticationHandler = this.getAccessHandler(source);
		return socialAuthenticationHandler.preProcess(core, params);
	}

	public AccessResponse preProcess(AccountType accountType, String core, String... params) {
		AccessHandler socialAuthenticationHandler = this.getAccessHandler(accountType);
		return socialAuthenticationHandler.preProcess(core, params);
	}

	public AccessUserDetails findAccessUserDetails(String source, AccessPrincipal accessPrincipal) {
		AccessHandler socialAuthenticationHandler = this.getAccessHandler(source);
		AccessUserDetails accessUserDetails = socialAuthenticationHandler.loadUserDetails(source, accessPrincipal);

		log.debug("[Herodotus] |- AccessHandlerFactory findAccessUserDetails.");
		return accessUserDetails;
	}

	public AccessHandler getAccessHandler(String source) {
		if (ObjectUtils.isEmpty(source)) {
			throw new IllegalAccessArgumentException("Cannot found SocialProvider");
		}

		AccountType accountType = AccountType.getAccountType(source);
		if (ObjectUtils.isEmpty(accountType)) {
			throw new IllegalAccessArgumentException("Cannot parse the source parameter.");
		}

		return getAccessHandler(accountType);
	}

	public AccessHandler getAccessHandler(AccountType accountType) {
		String handlerName = accountType.getHandler();
		AccessHandler socialAuthenticationHandler = handlers.get(handlerName);
		if (ObjectUtils.isNotEmpty(socialAuthenticationHandler)) {
			return socialAuthenticationHandler;
		} else {
			throw new AccessHandlerNotFoundException("Can not found Social Handler for " + handlerName);
		}
	}
}
