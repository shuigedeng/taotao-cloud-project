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

package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.data.mybatis.base.entity.MpSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户第三方登录信息配置表
 *
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_just_auth_source")
@Schema(description = "租户第三方登录信息配置表")
public class JustAuthSource extends MpSuperEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "第三方登录的名称")
    @TableField("source_name")
    private String sourceName;

    @Schema(description = "第三方登录类型：默认default  自定义custom")
    @TableField("source_type")
    private String sourceType;

    @Schema(description = "自定义第三方登录的请求Class")
    @TableField("request_class")
    private String requestClass;

    @Schema(description = "客户端id：对应各平台的appKey")
    @TableField("client_id")
    private String clientId;

    @Schema(description = "客户端Secret：对应各平台的appSecret")
    @TableField("client_secret")
    private String clientSecret;

    @Schema(description = "登录成功后的回调地址")
    @TableField("redirect_uri")
    private String redirectUri;

    @Schema(description = "支付宝公钥：当选择支付宝登录时，该值可用")
    @TableField("alipay_public_key")
    private String alipayPublicKey;

    @Schema(description = "是否需要申请unionid，目前只针对qq登录")
    @TableField("union_id")
    private Boolean unionId;

    @Schema(description = "Stack Overflow Key")
    @TableField("stack_overflow_key")
    private String stackOverflowKey;

    @Schema(description = "企业微信，授权方的网页应用ID")
    @TableField("agent_id")
    private String agentId;

    @Schema(description = "企业微信第三方授权用户类型，member|admin")
    @TableField("user_type")
    private String userType;

    @Schema(description = "域名前缀 使用 Coding 登录和 Okta 登录时，需要传该值。")
    @TableField("domain_prefix")
    private String domainPrefix;

    @Schema(description = "忽略校验code state 参数，默认不开启。")
    @TableField("ignore_check_state")
    private Boolean ignoreCheckState;

    @Schema(description = "支持自定义授权平台的 scope 内容")
    @TableField("scopes")
    private String scopes;

    @Schema(description = "设备ID, 设备唯一标识ID")
    @TableField("device_id")
    private String deviceId;

    @Schema(description = "喜马拉雅：客户端操作系统类型，1-iOS系统，2-Android系统，3-Web")
    @TableField("client_os_type")
    private Integer clientOsType;

    @Schema(description = "喜马拉雅：客户端包名")
    @TableField("pack_id")
    private String packId;

    @Schema(description = " 是否开启 PKCE 模式，该配置仅用于支持 PKCE 模式的平台，针对无服务应用，不推荐使用隐式授权，推荐使用 PKCE 模式")
    @TableField("pkce")
    private Boolean pkce;

    @Schema(description = "Okta 授权服务器的 ID， 默认为 default。")
    @TableField("auth_server_id")
    private String authServerId;

    @Schema(description = "忽略校验 {@code redirectUri} 参数，默认不开启。")
    @TableField("ignore_check_redirect_uri")
    private Boolean ignoreCheckRedirectUri;

    @Schema(description = "Http代理类型")
    @TableField("proxy_type")
    private String proxyType;

    @Schema(description = "Http代理Host")
    @TableField("proxy_host_name")
    private String proxyHostName;

    @Schema(description = "Http代理Port")
    @TableField("proxy_port")
    private Integer proxyPort;

    @Schema(description = "'0'禁用，'1' 启用")
    @TableField("status")
    private Integer status;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;
}
