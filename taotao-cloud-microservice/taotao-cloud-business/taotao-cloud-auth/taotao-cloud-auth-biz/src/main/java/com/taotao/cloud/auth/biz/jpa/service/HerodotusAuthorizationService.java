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

package com.taotao.cloud.auth.biz.jpa.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.auth.biz.jpa.entity.HerodotusAuthorization;
import com.taotao.cloud.auth.biz.jpa.repository.HerodotusAuthorizationRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:10:00
 */
@Service
public class HerodotusAuthorizationService {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(HerodotusAuthorizationService.class);

    /**
     * 希罗多德授权库
     */
    private final HerodotusAuthorizationRepository herodotusAuthorizationRepository;

    /**
     * 希罗多德授权服务
     *
     * @param herodotusAuthorizationRepository 希罗多德授权库
     * @since 2023-07-10 17:10:00
     */
    @Autowired
    public HerodotusAuthorizationService(HerodotusAuthorizationRepository herodotusAuthorizationRepository) {
        this.herodotusAuthorizationRepository = herodotusAuthorizationRepository;
    }

    /**
     * 按状态查找
     *
     * @param state 州
     * @return {@link Optional }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:01
     */
    public Optional<HerodotusAuthorization> findByState(String state) {
        Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findByState(state);
        log.info("HerodotusAuthorization Service findByState.");
        return result;
    }

    /**
     * 按授权码查找
     *
     * @param authorizationCode 授权代码
     * @return {@link Optional }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:01
     */
    public Optional<HerodotusAuthorization> findByAuthorizationCode(String authorizationCode) {
        Optional<HerodotusAuthorization> result =
                this.herodotusAuthorizationRepository.findByAuthorizationCodeValue(authorizationCode);
        log.info("HerodotusAuthorization Service findByAuthorizationCode.");
        return result;
    }

    /**
     * 通过访问令牌查找
     *
     * @param accessToken 访问令牌
     * @return {@link Optional }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:02
     */
    public Optional<HerodotusAuthorization> findByAccessToken(String accessToken) {
        Optional<HerodotusAuthorization> result =
                this.herodotusAuthorizationRepository.findByAccessTokenValue(accessToken);
        log.info("HerodotusAuthorization Service findByAccessToken.");
        return result;
    }

    /**
     * 通过刷新令牌查找
     *
     * @param refreshToken 刷新令牌
     * @return {@link Optional }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:03
     */
    public Optional<HerodotusAuthorization> findByRefreshToken(String refreshToken) {
        Optional<HerodotusAuthorization> result =
                this.herodotusAuthorizationRepository.findByRefreshTokenValue(refreshToken);
        log.info("HerodotusAuthorization Service findByRefreshToken.");
        return result;
    }

    /**
     * 按oidc标识令牌值查找
     *
     * @param idToken id令牌
     * @return {@link Optional }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:03
     */
    public Optional<HerodotusAuthorization> findByOidcIdTokenValue(String idToken) {
        Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findByOidcIdTokenValue(idToken);
        log.info("HerodotusAuthorization Service findByOidcIdTokenValue.");
        return result;
    }

    /**
     * 按用户代码值查找
     *
     * @param userCode 用户代码
     * @return {@link Optional }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:04
     */
    public Optional<HerodotusAuthorization> findByUserCodeValue(String userCode) {
        Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findByUserCodeValue(userCode);
        log.info("HerodotusAuthorization Service findByUserCodeValue.");
        return result;
    }

    /**
     * 按设备代码值查找
     *
     * @param deviceCode 设备代码
     * @return {@link Optional }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:05
     */
    public Optional<HerodotusAuthorization> findByDeviceCodeValue(String deviceCode) {
        Optional<HerodotusAuthorization> result =
                this.herodotusAuthorizationRepository.findByDeviceCodeValue(deviceCode);
        log.info("HerodotusAuthorization Service findByDeviceCodeValue.");
        return result;
    }

    /**
     * 按状态或授权代码值或访问令牌值或刷新令牌值或oidc id令牌值或用户代码值或设备代码值查找
     *
     * @param token 令牌
     * @return {@link Optional }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:05
     */
    public Optional<HerodotusAuthorization>
            findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
                    String token) {

        Specification<HerodotusAuthorization> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("state"), token));
            predicates.add(criteriaBuilder.equal(root.get("authorizationCodeValue"), token));
            predicates.add(criteriaBuilder.equal(root.get("accessTokenValue"), token));
            predicates.add(criteriaBuilder.equal(root.get("refreshTokenValue"), token));
            predicates.add(criteriaBuilder.equal(root.get("oidcIdTokenValue"), token));
            predicates.add(criteriaBuilder.equal(root.get("userCodeValue"), token));
            predicates.add(criteriaBuilder.equal(root.get("deviceCodeValue"), token));

            Predicate[] predicateArray = new Predicate[predicates.size()];
            criteriaQuery.where(criteriaBuilder.or(predicates.toArray(predicateArray)));
            return criteriaQuery.getRestriction();
        };

        Optional<HerodotusAuthorization> result = this.herodotusAuthorizationRepository.findOne(specification);
        log.info("HerodotusAuthorization Service findByDetection.");
        return result;
    }

    /**
     * 清除历史标记
     *
     * @since 2023-07-10 17:10:06
     */
    public void clearHistoryToken() {
        this.herodotusAuthorizationRepository.deleteByRefreshTokenExpiresAtBefore(LocalDateTime.now());
        log.info("HerodotusAuthorization Service clearExpireAccessToken.");
    }

    /**
     * 查找可用授权
     *
     * @param registeredClientId 注册客户端id
     * @param principalName      主体名称
     * @return {@link List }<{@link HerodotusAuthorization }>
     * @since 2023-07-10 17:10:06
     */
    public List<HerodotusAuthorization> findAvailableAuthorizations(String registeredClientId, String principalName) {
        List<HerodotusAuthorization> authorizations = this.herodotusAuthorizationRepository
                .findAllByRegisteredClientIdAndPrincipalNameAndAccessTokenExpiresAtAfter(
                        registeredClientId, principalName, LocalDateTime.now());
        log.info("HerodotusAuthorization Service findAvailableAuthorizations.");
        return authorizations;
    }

    /**
     * 查找授权计数
     *
     * @param registeredClientId 注册客户端id
     * @param principalName      主体名称
     * @return int
     * @since 2023-07-10 17:10:07
     */
    public int findAuthorizationCount(String registeredClientId, String principalName) {
        List<HerodotusAuthorization> authorizations = findAvailableAuthorizations(registeredClientId, principalName);
        int count = 0;
        if (CollectionUtils.isNotEmpty(authorizations)) {
            count = authorizations.size();
        }
        log.info("HerodotusAuthorization Service current authorization count is [{}].", count);
        return count;
    }

    /**
     * @param entity 实体
     * @since 2023-07-10 17:10:08
     */
    public void saveAndFlush(HerodotusAuthorization entity) {
        herodotusAuthorizationRepository.save(entity);
    }

    /**
     * 按id删除
     *
     * @param id id
     * @since 2023-07-10 17:10:08
     */
    public void deleteById(String id) {
        herodotusAuthorizationRepository.deleteById(id);
    }

    /**
     * 按id查找
     *
     * @param id id
     * @return {@link HerodotusAuthorization }
     * @since 2023-07-10 17:10:09
     */
    public HerodotusAuthorization findById(String id) {
        return herodotusAuthorizationRepository.findById(id).orElse(null);
    }

    /**
     * 更新和刷新
     *
     * @param entity 实体
     * @since 2023-07-10 17:10:09
     */
    public void updateAndFlush(HerodotusAuthorization entity) {
        HerodotusAuthorization existingAuthorization = this.findById(entity.getId());
        BeanUtil.copyProperties(
                entity, existingAuthorization, CopyOptions.create().ignoreNullValue());
        // 更新数据
        herodotusAuthorizationRepository.updateBy(existingAuthorization);
    }
}
