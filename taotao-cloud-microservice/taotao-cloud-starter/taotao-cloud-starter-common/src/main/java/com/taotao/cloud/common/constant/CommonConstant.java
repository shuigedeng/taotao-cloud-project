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
package com.taotao.cloud.common.constant;

import java.time.format.DateTimeFormatter;

/**
 * 全局公共常量
 *
 * @author dengtao
 * @date 2020/4/30 10:23
 * @since v1.0
 */
public interface CommonConstant {
    /**
     * 邮箱
     */
    String RESET_MAIL = "MAIL";

    /**
     * 图形验证码
     */
    String TAOTAO_CAPTCHA_KEY = "taotao_captcha_key:";

    /**
     * 短信验证码
     */
    String SMS_KEY = "SMS_CODE_";

    /**
     * 项目版本号(banner使用)
     */
    String PROJECT_VERSION = "1.0";

    /**
     * token请求头名称
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * ACCESS_TOKEN
     */
    String ACCESS_TOKEN = "access_token";

    /**
     * Bearer
     */
    String BEARER_TYPE = "Bearer";

    /**
     * 标签且 名称
     */
    String LABEL_AND = "labelAnd";

    /**
     * 权重key
     */
    String WEIGHT_KEY = "weight";

    /**
     * 删除
     */
    String STATUS_DEL = "1";

    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 目录
     */
    Integer CATALOG = -1;

    /**
     * 菜单
     */
    Integer MENU = 1;

    /**
     * 权限
     */
    Integer PERMISSION = 2;

    /**
     * 删除标记
     */
    String DEL_FLAG = "is_del";

    /**
     * 超级管理员用户名
     */
    String ADMIN_USER_NAME = "admin";


    /**
     * 租户id参数
     */
    String TENANT_ID = "t-tenantId";

    /**
     * 租户信息头
     */
    String TENANT_HEADER = "t-tenant-header";

    /**
     * 日志链路追踪id
     */
    String TRACE_ID = "t-traceId";

    /**
     * 日志链路追踪id信息头
     */
    String TRACE_HEADER = "t-traceId-header";

    /**
     * 负载均衡策略-版本号 信息头
     */
    String T_VERSION = "t-version";

    /**
     * 注册中心元数据 版本号
     */
    String METADATA_VERSION = "version";

    /**
     * 用户信息头
     */
    String USER_HEADER = "t-user-header";

    /**
     * 用户name信息头
     */
    String USER_NAME_HEADER = "t-user-name-header";

    /**
     * 用户id信息头
     */
    String USER_ID_HEADER = "t-userId-header";

    /**
     * 角色信息头
     */
    String ROLE_HEADER = "t-role-header";

    /**
     * grant_type
     */
    String GRANT_TYPE = "grant_type";
    /**
     * login_type
     */
    String USER_TYPE = "user_type";
    /**
     * 自定义登录接口
     */
    String CUSTOM_OAUTH_LOGIN = "/oauth/login";
    /**
     * 手机登录
     */
    String PHONE = "phone";
    /**
     * 手机验证码
     */
    String VERIFY_CODE = "verify_code";
    /**
     * 二维码
     */
    String QR_CODE = "qr_code";
    /**
     * 用户
     */
    String USERNAME = "username";
    /**
     * 密码
     */
    String PASSWORD = "password";

    // ******************** 用户类型 *******************
    /**
     * 会员用户
     */
    String MEMBER_USER = "member";
    /**
     * 后台管理用户
     */
    String BACKEND_USER = "backend";

    // ******************** 用户登录类型 *******************
    /**
     * 手机登录
     */
    String PHONE_LOGIN = "phone";
    /**
     * 二维码登录
     */
    String QR_LOGIN = "qr";
    /**
     * 账号密码登录
     */
    String PASSWORD_LOGIN = "password";

    // ******************** 公共日期格式 *******************
    String MONTH_FORMAT = "yyyy-MM";
    String DATE_FORMAT = "yyyy-MM-dd";
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String SIMPLE_MONTH_FORMAT = "yyyyMM";
    String SIMPLE_DATE_FORMAT = "yyyyMMdd";
    String SIMPLE_DATETIME_FORMAT = "yyyyMMddHHmmss";
    String DEF_USER_PASSWORD = "123456";
    String LOCK_KEY_PREFIX = "LOCK_KEY:";
    DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    // ******************** 操作日志类型 *******************
    /**
     * 查询/获取
     */
    int OPERATE_TYPE_1 = 1;

    /**
     * 添加
     */
    int OPERATE_TYPE_2 = 2;

    /**
     * 更新
     */
    int OPERATE_TYPE_3 = 3;

    /**
     * 删除
     */
    int OPERATE_TYPE_4 = 4;

    Long RESOURCE_TREE_ROOT_ID = 0L;

    String SUCCESS = "success";
    String ERROR = "error";
	String TIME_ZONE_GMT8 = "GMT+8";
}
