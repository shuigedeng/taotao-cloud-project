/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.auth.api.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 客户端查询query
 *
 * @author dengtao
 * @since 2020/5/14 17:05
 * @version 1.0.0
 */
@Data
@Builder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "客户端查询query")
public class ClientQuery implements Serializable {

    private static final long serialVersionUID = -7605952923416404638L;

    @ApiModelProperty(value = "应用标识")
    private String clientId;
    @ApiModelProperty(value = "应用名称")
    private String clientName;
    @ApiModelProperty(value = "资源ID")
    private String resourceIds;
    @ApiModelProperty(value = "客户端密钥")
    private String clientSecret;
    @ApiModelProperty(value = "客户端密钥(明文)")
    private String clientSecretStr;
    @ApiModelProperty(value = "作用域")
    private String scope;
    @ApiModelProperty(value = "授权方式")
    private String authorizedGrantTypes;
    @ApiModelProperty(value = "客户端重定向uri")
    private String webServerRedirectUri;
    @ApiModelProperty(value = "权限范围")
    private String authorities;
    @ApiModelProperty(value = "请求令牌有效时间")
    private Integer accessTokenValiditySeconds;
    @ApiModelProperty(value = "刷新令牌有效时间")
    private Integer refreshTokenValiditySeconds;
    @ApiModelProperty(value = "扩展信息")
    private String additionalInformation;
    @ApiModelProperty(value = "是否自动放行")
    private String autoapprove;
}
