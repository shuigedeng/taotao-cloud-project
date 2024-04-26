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

package com.taotao.cloud.auth.infrastructure.authorization.service;

import com.taotao.cloud.auth.infrastructure.persistent.authorization.po.TtcAuthorizationConsent;
import com.taotao.cloud.auth.infrastructure.persistent.authorization.repository.TtcAuthorizationConsentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>TtcAuthorizationConsentService </p>
 * <p>
 * 这里命名没有按照统一的习惯，主要是为了防止与 spring-authorization-server 已有类的同名而导致Bean注入失败
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:09:44
 */
@Service
public class TtcAuthorizationConsentService {

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(
		TtcAuthorizationConsentService.class);

	/**
	 * 授权同意存储库
	 */
	private final TtcAuthorizationConsentRepository authorizationConsentRepository;

	/**
	 * 希罗多德授权同意服务
	 *
	 * @param authorizationConsentRepository 授权同意存储库
	 * @since 2023-07-10 17:09:45
	 */
	@Autowired
	public TtcAuthorizationConsentService(
		TtcAuthorizationConsentRepository authorizationConsentRepository) {
		this.authorizationConsentRepository = authorizationConsentRepository;
	}

	/**
	 * 按注册客户端id和主体名称查找
	 *
	 * @param registeredClientId 注册客户端id
	 * @param principalName      主体名称
	 * @return {@link Optional }<{@link TtcAuthorizationConsent }>
	 * @since 2023-07-10 17:09:46
	 */
	public Optional<TtcAuthorizationConsent> findByRegisteredClientIdAndPrincipalName(
		String registeredClientId, String principalName) {
		Optional<TtcAuthorizationConsent> result =
			this.authorizationConsentRepository.findByRegisteredClientIdAndPrincipalName(
				registeredClientId, principalName);
		log.info("TtcAuthorizationConsent Service findByRegisteredClientIdAndPrincipalName.");
		return result;
	}

	/**
	 * 通过注册客户端id和主体名称删除
	 *
	 * @param registeredClientId 注册客户端id
	 * @param principalName      主体名称
	 * @since 2023-07-10 17:09:47
	 */
	public void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId,
		String principalName) {
		this.authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
			registeredClientId, principalName);
		log.info(
			"TtcAuthorizationConsent Service deleteByRegisteredClientIdAndPrincipalName.");
	}

	/**
	 * 保存
	 *
	 * @param entity 实体
	 * @since 2023-07-10 17:09:47
	 */
	public void save(TtcAuthorizationConsent entity) {
		authorizationConsentRepository.save(entity);
	}

//	public Page<TtcAuthorizationConsent> myPageQuery(String registeredClientId,
//		String principalName) {
//		return authorizationConsentRepository.myPageQuery(registeredClientId, principalName,
//			PageRequest.of(0, 20));
//	}
}
