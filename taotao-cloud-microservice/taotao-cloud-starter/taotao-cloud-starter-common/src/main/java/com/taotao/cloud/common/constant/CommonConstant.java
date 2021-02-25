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
 * @version 1.0.0
 * @since 2020/4/30 10:23
 */
public class CommonConstant {

	private CommonConstant() {
	}

	/**
	 * 邮箱
	 */
	public static final String RESET_MAIL = "MAIL";

	/**
	 * 图形验证码
	 */
	public static final String TAOTAO_CAPTCHA_KEY = "taotao_captcha_key:";

	/**
	 * 短信验证码
	 */
	public static final String SMS_KEY = "SMS_CODE_";

	/**
	 * 项目版本号(banner使用)
	 */
	public static final String PROJECT_VERSION = "1.0";

	/**
	 * token请求头名称
	 */
	public static final String TOKEN_HEADER = "Authorization";

	/**
	 * ACCESS_TOKEN
	 */
	public static final String ACCESS_TOKEN = "access_token";

	/**
	 * Bearer
	 */
	public static final String BEARER_TYPE = "Bearer";

	/**
	 * 标签且 名称
	 */
	public static final String LABEL_AND = "labelAnd";

	/**
	 * 权重key
	 */
	public static final String WEIGHT_KEY = "weight";

	/**
	 * 删除
	 */
	public static final String STATUS_DEL = "1";

	/**
	 * 正常
	 */
	public static final String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	public static final String STATUS_LOCK = "9";

	/**
	 * 目录
	 */
	public static final Integer CATALOG = -1;

	/**
	 * 菜单
	 */
	public static final Integer MENU = 1;

	/**
	 * 权限
	 */
	public static final Integer PERMISSION = 2;

	/**
	 * 删除标记
	 */
	public static final String DEL_FLAG = "is_del";

	/**
	 * 超级管理员用户名
	 */
	public static final String ADMIN_USER_NAME = "admin";


	/**
	 * 租户id参数
	 */
	public static final String TENANT_ID = "t-tenantId";

	/**
	 * 租户信息头
	 */
	public static final String TENANT_HEADER = "t-tenant-header";

	/**
	 * 日志链路追踪id
	 */
	public static final String TRACE_ID = "t-traceId";

	/**
	 * 日志链路追踪id信息头
	 */
	public static final String TRACE_HEADER = "t-traceId-header";

	/**
	 * 负载均衡策略-版本号 信息头
	 */
	public static final String T_VERSION = "t-version";

	/**
	 * 注册中心元数据 版本号
	 */
	public static final String METADATA_VERSION = "version";

	/**
	 * 用户信息头
	 */
	public static final String USER_HEADER = "t-user-header";

	/**
	 * 用户name信息头
	 */
	public static final String USER_NAME_HEADER = "t-user-name-header";

	/**
	 * 用户id信息头
	 */
	public static final String USER_ID_HEADER = "t-userId-header";

	/**
	 * 角色信息头
	 */
	public static final String ROLE_HEADER = "t-role-header";

	/**
	 * grant_type
	 */
	public static final String GRANT_TYPE = "grant_type";
	/**
	 * login_type
	 */
	public static final String USER_TYPE = "user_type";
	/**
	 * 自定义登录接口
	 */
	public static final String CUSTOM_OAUTH_LOGIN = "/oauth/login";
	/**
	 * 手机登录
	 */
	public static final String PHONE = "phone";
	/**
	 * 手机验证码
	 */
	public static final String VERIFY_CODE = "verify_code";
	/**
	 * 二维码
	 */
	public static final String QR_CODE = "qr_code";
	/**
	 * 用户
	 */
	public static final String USERNAME = "username";
	/**
	 * 密码
	 */
	public static final String PASSWORD = "password";

	// ******************** 用户类型 *******************
	/**
	 * 会员用户
	 */
	public static final String MEMBER_USER = "member";
	/**
	 * 后台管理用户
	 */
	public static final String BACKEND_USER = "backend";

	// ******************** 用户登录类型 *******************
	/**
	 * 手机登录
	 */
	public static final String PHONE_LOGIN = "phone";
	/**
	 * 二维码登录
	 */
	public static final String QR_LOGIN = "qr";
	/**
	 * 账号密码登录
	 */
	public static final String PASSWORD_LOGIN = "password";

	// ******************** 公共日期格式 *******************
	public static final String MONTH_FORMAT = "yyyy-MM";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SIMPLE_MONTH_FORMAT = "yyyyMM";
	public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";
	public static final String SIMPLE_DATETIME_FORMAT = "yyyyMMddHHmmss";
	public static final String DEF_USER_PASSWORD = "123456";
	public static final String LOCK_KEY_PREFIX = "LOCK_KEY:";
	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter
		.ofPattern(DATETIME_FORMAT);

	// ******************** 操作日志类型 *******************
	/**
	 * 查询/获取
	 */
	public static final int OPERATE_TYPE_1 = 1;

	/**
	 * 添加
	 */
	public static final int OPERATE_TYPE_2 = 2;

	/**
	 * 更新
	 */
	public static final int OPERATE_TYPE_3 = 3;

	/**
	 * 删除
	 */
	public static final int OPERATE_TYPE_4 = 4;

	public static final Long RESOURCE_TREE_ROOT_ID = 0L;

	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String TIME_ZONE_GMT8 = "GMT+8";
}
