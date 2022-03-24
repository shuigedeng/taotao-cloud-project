///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.oauth2.api.oauth2_server.entity;
//
//import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
//import javax.persistence.Column;
//import javax.persistence.Table;
//
///**
// * Client
// *
// * @author shuigedeng
// * @since 2020/5/2 11:17
// * @version 2022.03
// */
//@Table(name = "oauth_client_details")
//@org.hibernate.annotations.Table(appliesTo = "oauth_client_details", comment = "客户端表")
//public class Client extends JpaSuperEntity {
//    /**
//     * 用于唯一标识每一个客户端(client)
//     */
//    @Column(name = "client_id", nullable = false, unique = true, columnDefinition = "varchar(32) NOT NULL COMMENT '应用标识'")
//    private String clientId;
//    /**
//     * 应用名称
//     */
//    @Column(name = "client_name", nullable = false, columnDefinition = "varchar(128)  NOT NULL COMMENT '应用名称'")
//    private String clientName;
//    /**
//     * 客户端密钥
//     */
//    @Column(name = "client_secret", nullable = false, columnDefinition = "varchar(256)  NOT NULL COMMENT '应用密钥(bcyt) 加密'")
//    private String clientSecret;
//    /**
//     * 客户端密钥(明文)
//     */
//    @Column(name = "client_secret_str", nullable = false, columnDefinition = "varchar(256)  NOT NULL COMMENT '应用密钥(明文)'")
//    private String clientSecretStr;
//    /**
//     * 资源ID
//     */
//    @Builder.Default
//    @Column(name = "resource_ids", columnDefinition = "varchar(256) NULL DEFAULT '' COMMENT '资源限定串(逗号分割)'")
//    private String resourceIds = "";
//    /**
//     * 作用域
//     */
//    @Builder.Default
//    @Column(name = "scope", columnDefinition = "varchar(256) NULL default 'all' COMMENT '作用域'")
//    private String scope = "all";
//    /**
//     * 授权方式（A,B,C）
//     */
//    @Builder.Default
//    @Column(name = "authorized_grant_types", columnDefinition = "varchar(256) NULL default 'authorization_code,password,refresh_token,client_credentials' COMMENT '4种oauth授权方式(authorization_code,password,refresh_token,client_credentials)'")
//    private String authorizedGrantTypes = "authorization_code,password,refresh_token,client_credentials";
//    /**
//     * 客户端重定向uri
//     */
//    @Column(name = "web_server_redirect_uri", columnDefinition = "varchar(256) NULL DEFAULT NULL COMMENT '回调地址 '")
//    private String webServerRedirectUri;
//    /**
//     * 指定用户的权限范围
//     */
//    @Builder.Default
//    @Column(name = "authorities", columnDefinition = "varchar(256) NULL DEFAULT '' COMMENT '权限范围'")
//    private String authorities = "";
//    /**
//     * 请求令牌有效时间 设置access_token的有效时间(秒),默认(606012,12小时)
//     */
//    @Builder.Default
//    @Column(name = "access_token_validity", columnDefinition = "int(11) NULL DEFAULT 18000 COMMENT 'access_token有效期'")
//    private Integer accessTokenValiditySeconds = 18000;
//    /**
//     * 刷新令牌有效时间 设置refresh_token有效期(秒)，默认(606024*30, 30填)
//     */
//    @Builder.Default
//    @Column(name = "refresh_token_validity", columnDefinition = "int(11) NULL DEFAULT 28800 COMMENT 'refresh_token有效期'")
//    private Integer refreshTokenValiditySeconds = 28800;
//    /**
//     * 扩展信息 值必须是json格式
//     */
//    @Builder.Default
//    @Column(name = "additional_information", columnDefinition = "varchar(4096) NULL DEFAULT '{}' COMMENT '{}'")
//    private String additionalInformation = "{}";
//    /**
//     * 是否自动放行 默认false,适用于authorization_code模式,设置用户是否自动approval操作,设置true跳过用户确认授权操作页面，直接跳到redirect_uri
//     */
//    @Builder.Default
//    @Column(name = "autoapprove", columnDefinition = "char(5) NULL DEFAULT 'true' COMMENT '是否自动授权 是-true'")
//    private String autoapprove = "true";
//}
