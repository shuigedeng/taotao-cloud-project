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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.repository;

import com.taotao.cloud.auth.infrastructure.persistent.authorization.generator.TtcAuthorizationConsentId;
import com.taotao.cloud.auth.infrastructure.persistent.authorization.po.TtcAuthorizationConsent;
import com.taotao.boot.data.jpa.base.repository.JpaInterfaceSuperRepository;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.QueryHints;

/**
 * <p>TtcAuthorizationConsentRepository </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:11:17
 */
public interface TtcAuthorizationConsentRepository
	extends
	JpaInterfaceSuperRepository<TtcAuthorizationConsent, TtcAuthorizationConsentId> {

	/**
	 * 根据 client id 和 principalName 查询 OAuth2 确认信息
	 *
	 * @param registeredClientId 注册OAuth2客户端ID
	 * @param principalName      用户名
	 * @return {@link Optional }<{@link TtcAuthorizationConsent }>
	 * @since 2023-07-10 17:11:17
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<TtcAuthorizationConsent> findByRegisteredClientIdAndPrincipalName(
		String registeredClientId, String principalName);

	/**
	 * 根据 client id 和 principalName 删除 OAuth2 确认信息
	 *
	 * @param registeredClientId 注册OAuth2客户端ID
	 * @param principalName      用户名
	 * @since 2023-07-10 17:11:17
	 */
	void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId,
		String principalName);

//	@MyQuery(value = """
//		select h
//		from TtcAuthorizationConsent h
//		where ?{ registeredClientId = :registeredClientId }
//		?{ and principalName = :principalName }
//		""")
//	Page<TtcAuthorizationConsent> myPageQuery(
//		@Param("registeredClientId") String registeredClientId,
//		@Param("principalName") String principalName,
//		PageRequest pageRequest);
}
