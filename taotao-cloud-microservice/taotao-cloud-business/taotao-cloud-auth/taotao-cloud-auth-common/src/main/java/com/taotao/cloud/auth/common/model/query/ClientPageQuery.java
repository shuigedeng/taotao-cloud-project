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

package com.taotao.cloud.auth.common.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 客户端分页查询Query
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/14 17:05
 */
@Schema(description = "客户端分页查询Query")
public class ClientPageQuery implements Serializable {

	@Serial
	private static final long serialVersionUID = 5953949488238258726L;

	@Schema(description = "应用标识")
	private String clientId;

	@Schema(description = "应用名称")
	private String clientName;

	@Schema(description = "资源ID")
	private String resourceIds;

	@Schema(description = "客户端密钥")
	private String clientSecret;

	@Schema(description = "客户端密钥(明文)")
	private String clientSecretStr;

	@Schema(description = "作用域")
	private String scope;

	@Schema(description = "授权方式")
	private String authorizedGrantTypes;

	@Schema(description = "客户端重定向uri")
	private String webServerRedirectUri;

	@Schema(description = "权限范围")
	private String authorities;

	@Schema(description = "请求令牌有效时间")
	private Integer accessTokenValiditySeconds;

	@Schema(description = "刷新令牌有效时间")
	private Integer refreshTokenValiditySeconds;

	@Schema(description = "扩展信息")
	private String additionalInformation;

	@Schema(description = "是否自动放行")
	private String autoapprove;
}
