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

/**
 * Security 公共常量
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 20:23
 */
public class SecurityConstant {

	private SecurityConstant() {
	}

	public static final String OAUTH_TOKEN_URL = "/oauth/token/user";

	public static final String AUTHORIZED = "Authorized";

	public static final String BASE_AUTHORIZED = "BaseAuthorized";

	public static final String PHONE_KEY = "phone";

	public static final String SMS_CODE = "smsCode";

	/**
	 * sys_oauth_client_details 表的字段 {scrypt}
	 */
	public static final String CLIENT_FIELDS = "client_id, client_secret, resource_ids, scope, "
		+ "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
		+ "refresh_token_validity, additional_information, autoapprove";

	/**
	 * JdbcClientDetailsService 查询语句
	 */
	public static final String BASE_FIND = "select " + CLIENT_FIELDS + " from oauth_client_details";

	/**
	 * 默认的查询语句
	 */
	public static final String DEFAULT_FIND = BASE_FIND + " order by client_id";

	/**
	 * 按条件client_id 查询
	 */
	public static final String DEFAULT_SELECT = BASE_FIND + " where client_id = ?";

	public static final String NORMAL = "normal";
	public static final String SMS = "sms";
	public static final String LOGIN_QQ = "qq";
	public static final String LOGIN_WEIXIN = "weixin";
	public static final String LOGIN_GITEE = "gitee";
	public static final String LOGIN_GITHUB = "github";

	/**
	 * 基础角色
	 */
	public static final String BASE_ROLE = "ROLE_USER";

	/**
	 * 授权码模式
	 */
	public static final String AUTHORIZATION_CODE = "authorization_code";

	/**
	 * 密码模式
	 */
	public static final String PASSWORD = "password";

	/**
	 * 默认生成图形验证码宽度
	 */
	public static final String DEFAULT_IMAGE_WIDTH = "100";

	/**
	 * 默认生成图像验证码高度
	 */
	public static final String DEFAULT_IMAGE_HEIGHT = "35";

	/**
	 * 默认生成图形验证码长度
	 */
	public static final String DEFAULT_IMAGE_LENGTH = "4";

	/**
	 * 默认生成图形验证码过期时间
	 */
	public static final int DEFAULT_IMAGE_EXPIRE = 12000000;

	/**
	 * 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
	 */
	public static final String DEFAULT_COLOR_FONT = "blue";

	/**
	 * 图片边框
	 */
	public static final String DEFAULT_IMAGE_BORDER = "no";

	/**
	 * 默认图片间隔
	 */
	public static final String DEFAULT_CHAR_SPACE = "5";

	/**
	 * 默认保存手机验证码code的前缀
	 */
	public static final String DEFAULT_SMS_CODE_KEY = "default_sms_code_key";

	/**
	 * 验证码文字大小
	 */
	public static final String DEFAULT_IMAGE_FONT_SIZE = "30";

	/**
	 * 缓存client的redis key，这里是hash结构存储
	 */
	public static final String CACHE_CLIENT_KEY = "oauth_client_details";

	/**
	 * 默认token过期时间(1小时)
	 */
	public static final Integer ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 60;

	/**
	 * redis中授权token对应的key
	 */
	public static final String REDIS_TOKEN_AUTH = "auth:";

	/**
	 * redis中应用对应的token集合的key
	 */
	public static final String REDIS_CLIENT_ID_TO_ACCESS = "client_id_to_access:";

	/**
	 * redis中用户名对应的token集合的key
	 */
	public static final String REDIS_UNAME_TO_ACCESS = "uname_to_access:";

}
