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

package com.taotao.cloud.auth.biz.jpa.repository;

import com.taotao.cloud.auth.biz.jpa.entity.HerodotusAuthorizationConsent;
import com.taotao.cloud.auth.biz.jpa.generator.HerodotusAuthorizationConsentId;
import com.taotao.cloud.data.jpa.base.repository.BaseRepository;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.QueryHints;

/**
 * <p>Description: HerodotusAuthorizationConsentRepository </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:11:17
 */
public interface HerodotusAuthorizationConsentRepository
        extends BaseRepository<HerodotusAuthorizationConsent, HerodotusAuthorizationConsentId> {

	/**
	 * 根据 client id 和 principalName 查询 OAuth2 确认信息
	 *
	 * @param registeredClientId 注册OAuth2客户端ID
	 * @param principalName      用户名
	 * @return {@link Optional }<{@link HerodotusAuthorizationConsent }>
	 * @since 2023-07-10 17:11:17
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Optional<HerodotusAuthorizationConsent> findByRegisteredClientIdAndPrincipalName(
            String registeredClientId, String principalName);

	/**
	 * 根据 client id 和 principalName 删除 OAuth2 确认信息
	 *
	 * @param registeredClientId 注册OAuth2客户端ID
	 * @param principalName      用户名
	 * @since 2023-07-10 17:11:17
	 */
	void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
