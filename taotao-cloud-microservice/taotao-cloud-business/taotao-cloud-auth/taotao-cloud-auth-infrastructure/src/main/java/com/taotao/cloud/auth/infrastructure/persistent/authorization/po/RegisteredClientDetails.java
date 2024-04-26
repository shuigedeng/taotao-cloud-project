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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.po;

import java.time.LocalDateTime;

/**
 * <p>RegisteredClient 属性定义 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:13:27
 */
public interface RegisteredClientDetails {

	/**
	 * 得到id
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:13:27
	 */
	String getId();

	/**
	 * 让客户机id
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:13:27
	 */
	String getClientId();

	/**
	 * 在获得客户机id
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:13:27
	 */
	LocalDateTime getClientIdIssuedAt();

	/**
	 * 得到客户秘密
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:13:27
	 */
	String getClientSecret();

	/**
	 * 得到客户秘密到期
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:13:27
	 */
	LocalDateTime getClientSecretExpiresAt();

	/**
	 * 获取客户端身份验证方法
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:13:27
	 */
	String getClientAuthenticationMethods();

	/**
	 * 得到授权授予类型
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:13:27
	 */
	String getAuthorizationGrantTypes();

	/**
	 * 得到定向uri
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:13:27
	 */
	String getRedirectUris();

	/**
	 * 得到后注销重定向uri
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:13:27
	 */
	String getPostLogoutRedirectUris();
}
