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

package com.taotao.cloud.workflow.biz.common.model.login;

import lombok.Data;

/** 系统的核心基础信息 */
@Data
public class BaseSystemInfo {
    /** 单一登录：1-后登录踢出先登录、2-同时登陆 */
    private Integer singleLogin;
    /** 密码错误次数 */
    private Integer passwordErrorsNumber;
    /** 错误策略 1--账号锁定 2--延时登录 */
    private Integer lockType;
    /** 延时登录时间 */
    private Integer lockTime;
    /** 是否开启验证码 */
    private Integer enableVerificationCode;
    /** 验证码位数 */
    private Integer verificationCodeNumber;

    /** 超时登出时间小时 */
    private String tokenTimeout;
    /** 上次登录时间提示开关 */
    private Integer lastLoginTimeSwitch = 0;
    /** 公司电话 */
    private String companyTelePhone;
    /** appid */
    private String wxGzhAppId;
    /** 公司地址 */
    private String companyAddress;

    private String wxGzhAppSecret;

    private String qyhCorpSecret;

    private String isLog;

    private String emailSmtpPort;

    private String emailPop3Host;

    private String emailSenderName;
    /** 公司邮箱 */
    private String companyEmail;

    private String sysName;
    /** 版权信息 */
    private String copyright;

    private String qyhAgentId;

    private String lastLoginTime;

    private String emailAccount;

    private String qyhJoinUrl;

    private String whitelistSwitch;

    private String pageSize;
    /** 系统描述 */
    private String sysDescription;

    private String emailPassword;
    /** 公司法人 */
    private String companyContacts;
    /** 系统主题 */
    private String sysTheme;

    private String qyhAgentSecret;

    private String whitelistIp;
    /** 公司简称 */
    private String companyCode;

    private String emailSsl;

    private String emailSmtpHost;

    private String registerKey;

    private String wxGzhToken;

    private String qyhJoinTitle;

    private String qyhCorpId;
    /** 系统版本 */
    private String sysVersion;

    private String emailPop3Port;
    /** 公司名称 */
    private String companyName;

    private String wxGzhUrl;

    /** 企业微信-是否同步组织(包含：公司、部门) */
    private Integer qyhIsSynOrg;

    /** 企业微信-是否同步用户 */
    private Integer qyhIsSynUser;

    /** 钉钉同步公司-部门-用户的应用AppKey */
    private String dingSynAppKey;

    /** 钉钉同步公司-部门-用户的应用AppSecret */
    private String dingSynAppSecret;

    /** 钉钉-是否同步组织(包含：公司、部门) */
    private Integer dingSynIsSynOrg;

    /** 钉钉-是否同步用户 */
    private Integer dingSynIsSynUser;

    // 图标----
    private String loginIcon;

    private String logoIcon;

    private String appIcon;

    private String navigationIcon;
}
