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

package com.taotao.cloud.auth.biz.oauth2.jpa.service;

import com.taotao.cloud.auth.biz.oauth2.jpa.entity.HerodotusAuthorization;
import com.taotao.cloud.auth.biz.oauth2.jpa.repository.HerodotusAuthorizationRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * <p>Description: HerodotusAuthorizationService </p>
 * <p>
 * 这里命名没有按照统一的习惯，主要是为了防止与 spring-authorization-server 已有类的同名而导致Bean注入失败
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 21:06
 */
@Service
public class HerodotusAuthorizationService {
	//extends BaseLayeredService<HerodotusAuthorization, String> {

	private static final Logger log = LoggerFactory.getLogger(HerodotusAuthorizationService.class);

	private final HerodotusAuthorizationRepository herodotusAuthorizationRepository;

	@Autowired
	public HerodotusAuthorizationService(
		HerodotusAuthorizationRepository herodotusAuthorizationRepository) {
		this.herodotusAuthorizationRepository = herodotusAuthorizationRepository;
	}

	//@Override
	//public BaseRepository<HerodotusAuthorization, String> getRepository() {
	//	return this.herodotusAuthorizationRepository;
	//}

	public Optional<HerodotusAuthorization> findByState(String state) {
		Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findByState(
			state);
		log.debug("[Herodotus] |- HerodotusAuthorization Service findByState.");
		return result;
	}

	public Optional<HerodotusAuthorization> findByAuthorizationCode(String authorizationCode) {
		Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findByAuthorizationCode(
			authorizationCode);
		log.debug("[Herodotus] |- HerodotusAuthorization Service findByAuthorizationCode.");
		return result;
	}

	public Optional<HerodotusAuthorization> findByAccessToken(String accessToken) {
		Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findByAccessToken(
			accessToken);
		log.debug("[Herodotus] |- HerodotusAuthorization Service findByAccessToken.");
		return result;
	}

	public Optional<HerodotusAuthorization> findByRefreshToken(String refreshToken) {
		Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findByRefreshToken(
			refreshToken);
		log.debug("[Herodotus] |- HerodotusAuthorization Service findByRefreshToken.");
		return result;
	}

	public Optional<HerodotusAuthorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(
		String token) {

		Specification<HerodotusAuthorization> specification = (root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(root.get("state"), token));
			predicates.add(criteriaBuilder.equal(root.get("authorizationCode"), token));
			predicates.add(criteriaBuilder.equal(root.get("accessToken"), token));
			predicates.add(criteriaBuilder.equal(root.get("refreshToken"), token));

			Predicate[] predicateArray = new Predicate[predicates.size()];
			criteriaQuery.where(criteriaBuilder.or(predicates.toArray(predicateArray)));
			return criteriaQuery.getRestriction();
		};

		Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findOne(
			specification);
		log.debug("[Herodotus] |- HerodotusAuthorization Service findByDetection.");
		return result;
	}

	public void clearHistoryToken() {
		this.herodotusAuthorizationRepository.deleteByRefreshTokenExpiresAtBefore(
			LocalDateTime.now());
		log.debug("[Herodotus] |- HerodotusAuthorization Service clearExpireAccessToken.");
	}

	public List<HerodotusAuthorization> findAvailableAuthorizations(String registeredClientId,
		String principalName) {
		List<HerodotusAuthorization> authorizations = this.herodotusAuthorizationRepository.findAllByRegisteredClientIdAndPrincipalNameAndAccessTokenExpiresAtAfter(
			registeredClientId, principalName, LocalDateTime.now());
		log.debug("[Herodotus] |- HerodotusAuthorization Service findAvailableAuthorizations.");
		return authorizations;
	}

	public int findAuthorizationCount(String registeredClientId, String principalName) {
		List<HerodotusAuthorization> authorizations = findAvailableAuthorizations(
			registeredClientId, principalName);
		int count = 0;
		if (CollectionUtils.isNotEmpty(authorizations)) {
			count = authorizations.size();
		}
		log.debug(
			"[Herodotus] |- HerodotusAuthorization Service current authorization count is [{}].",
			count);
		return count;
	}
}
