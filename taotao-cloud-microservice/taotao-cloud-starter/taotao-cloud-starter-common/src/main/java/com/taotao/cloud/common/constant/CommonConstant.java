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
package com.taotao.cloud.common.constant;

import java.time.format.DateTimeFormatter;

/**
 * 全局公共常量
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:35:37
 */
public final class CommonConstant {

	private CommonConstant() {
	}

	/**
	 * utf-8
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * utf-16
	 */
	public static final String UTF16 = "UTF-16";

	/**
	 * gbk
	 */
	public static final String GBK = "GBK";

	/**
	 * 逗号
	 */
	public static final char COMMA = ',';

	/**
	 * 冒号
	 */
	public static final char COLON = ':';

	/**
	 * char 小数点
	 */
	public static final char DOT = '.';

	/**
	 * char 双引号
	 */
	public static final char DOUBLE_QUOTES = '"';

	/**
	 * char 反斜杠
	 */
	public static final char BACK_SLASH = '\\';

	/**
	 * char 空格
	 */
	public static final char BLANK = ' ';

	/**
	 * 星星
	 */
	public static final char STAR = '*';

	/**
	 * 文件分隔符
	 */
	public static final String PATH_SPLIT = "/";

	public static final boolean SUCCESS = true;
	public static final boolean ERROR = false;
	public static final String TIME_ZONE_GMT8 = "GMT+8";
	public static final String BEARER_TYPE = "bearea";

	public static final String TAOTAO = "taotao";
	public static final String CLOUD = "cloud";
	public static final String DEPENDENCIES = "depend";
	public static final String SPRING = "spring";
	public static final String BOOT = "boot";
	public static final String ALIBABA = "ali";
	public static final String UNDER = "_";

	/**
	 * banner 配置
	 */
	public static final String VERSION = "version";
	public static final String TAOTAO_CLOUD_VERSION =
		VERSION.toUpperCase() + COLON;

	public static final String SPRING_VERSION = "springVersion";
	public static final String TAOTAO_CLOUD_SPRING_VERSION =
		SPRING.toUpperCase() + UNDER
			+ VERSION.toUpperCase() + COLON;

	public static final String SPRING_BOOT_VERSION = "springBootVersion";
	public static final String TAOTAO_CLOUD_SPRING_BOOT_VERSION =
		 SPRING.toUpperCase() + UNDER
			+ BOOT.toUpperCase() + UNDER + VERSION.toUpperCase() + COLON;

	public static final String SPRING_CLOUD_VERSION = "springCloudVersion";
	public static final String TAOTAO_CLOUD_SPRING_CLOUD_VERSION =
		SPRING.toUpperCase() + UNDER
			+ CLOUD.toUpperCase() + UNDER + VERSION.toUpperCase() + COLON;

	public static final String SPRING_CLOUD_DEPENDENCIES_VERSION = "springCloudDependenciesVersion";
	public static final String TAOTAO_CLOUD_SPRING_CLOUD_DEPENDENCIES_VERSION =
		SPRING.toUpperCase() + UNDER
			+ CLOUD.toUpperCase() + UNDER + DEPENDENCIES.toUpperCase() + UNDER + VERSION.toUpperCase() + COLON;

	public static final String SPRING_CLOUD_ALIBABA_VERSION = "springCloudAlibabaVersion";
	public static final String TAOTAO_CLOUD_SPRING_CLOUD_ALIBABA_VERSION =
		SPRING.toUpperCase() + UNDER
			+ CLOUD.toUpperCase() + UNDER + ALIBABA.toUpperCase() + UNDER + VERSION.toUpperCase()
			+ COLON;

	public static final String TAOTAO_CLOUD_BANNER_DEFAULT_RESOURCE_LOCATION = "/banner/banner.txt";
	public static final String TAOTAO_CLOUD_BANNER_DEFAULT = "WELCOME TO TAOTAO CLOUD";
	public static final String TAOTAO_CLOUD_BANNER_GITHUB = "TAOTAO_CLOUD_GITHUB:";
	public static final String TAOTAO_CLOUD_BANNER_GITHUB_URL = "https://github.com/shuigedeng/taotao-cloud-project";
	public static final String TAOTAO_CLOUD_BANNER_GITEE = "TAOTAO_CLOUD_GITEE:";
	public static final String TAOTAO_CLOUD_BANNER_GITEE_URL = "https://gitee.com/dtbox/taotao-cloud-project";
	public static final String TAOTAO_CLOUD_BANNER_BLOG = "TAOTAO_CLOUD_BLOG:";
	public static final String TAOTAO_CLOUD_BANNER_BLOG_URL = "https://blog.taotaocloud.top/";
	public static final String TAOTAO_CLOUD_BANNER_DATAV = "TAOTAO_CLOUD_DATAV:";
	public static final String TAOTAO_CLOUD_BANNER_DATAV_URL = "https://datav.taotaocloud.top/";
	public static final String TAOTAO_CLOUD_BANNER_BACKEND = "TAOTAO_CLOUD_BACKEND:";
	public static final String TAOTAO_CLOUD_BANNER_BACKEND_URL = "https://backend.taotaocloud.top/";
	public static final String TAOTAO_CLOUD_BANNER_MANAGER = "TAOTAO_CLOUD_MANAGER:";
	public static final String TAOTAO_CLOUD_BANNER_MANAGER_URL = "https://manager.taotaocloud.top/";
	public static final String TAOTAO_CLOUD_BANNER_MERCHANT = "TAOTAO_CLOUD_MERCHANT:";
	public static final String TAOTAO_CLOUD_BANNER_MERCHANT_URL = "https://merchant.taotaocloud.top/";
	public static final String TAOTAO_CLOUD_BANNER_OPEN = "TAOTAO_CLOUD_OPEN:";
	public static final String TAOTAO_CLOUD_BANNER_OPEN_URL = "https://open.taotaocloud.top/";
	public static final String TAOTAO_CLOUD_BANNER_M = "TAOTAO_CLOUD_M:";
	public static final String TAOTAO_CLOUD_BANNER_M_URL = "https://m.taotaocloud.top/";
	public static final String TAOTAO_CLOUD_BANNER = "TAOTAO_CLOUD:";
	public static final String TAOTAO_CLOUD_BANNER_URL = "https://taotaocloud.top/";

	/**
	 * 请求头 配置
	 */
	public static final String TAOTAO_CLOUD_TENANT_ID = "taotao-cloud-tenant-id";
	public static final String TAOTAO_CLOUD_TENANT_ID_DEFAULT = "1";
	public static final String TAOTAO_CLOUD_TENANT_HEADER = "taotao-cloud-tenant-header";

	public static final String TAOTAO_CLOUD_TRACE_ID = "taotao-cloud-trace-id";
	public static final String TAOTAO_CLOUD_TRACE_HEADER = "taotao-cloud-trace-header";

	public static final String TAOTAO_CLOUD_REQUEST_VERSION = "taotao-cloud-request-version";
	public static final String TAOTAO_CLOUD_REQUEST_VERSION_HEADER = "taotao-cloud-request-version-header";

	public static final String TAOTAO_CLOUD_USER_HEADER = "taotao-cloud-user-header";
	public static final String TAOTAO_CLOUD_USER_NAME_HEADER = "taotao-cloud-user-name-header";
	public static final String TAOTAO_CLOUD_USER_ID_HEADER = "taotao-cloud-user-id-header";
	public static final String TAOTAO_CLOUD_USER_ROLE_HEADER = "taotao-cloud-user-role-header";

	/**
	 * 公共日期格式 配置
	 */
	public static final String MONTH_FORMAT = "yyyy-MM";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SIMPLE_MONTH_FORMAT = "yyyyMM";
	public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";
	public static final String SIMPLE_DATETIME_FORMAT = "yyyyMMddHHmmss";
	public static final String DEF_USER_PASSWORD = "123456";
	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter
		.ofPattern(DATETIME_FORMAT);

	public static final String YEAR_MONTH_FORMATTER = "yyyy-MM";
	public static final String DATE_FORMATTER = "yyyy-MM-dd";
	public static final String TIME_FORMATTER = "HH:mm:ss";
	public static final String YEAR_MONTH_FORMATTER_SHORT = "yyyyMM";
	public static final String DATE_FORMATTER_SHORT = "yyyyMMdd";
	public static final String DATETIME_FORMATTER_SHORT = "yyyyMMddHHmmss";
	public static final String TIME_FORMATTER_SHORT = "HHmmss";

	/**
	 * 操作日志类型 配置
	 */
	public static final int OPERATE_TYPE_GET = 1;
	public static final int OPERATE_TYPE_SAVE = 2;
	public static final int OPERATE_TYPE_UPDATE = 3;
	public static final int OPERATE_TYPE_DELETE = 4;

	/**
	 * 请求方式 配置
	 */
	public static final String GET = "GET";
	public static final String HEAD = "HEAD";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String PATCH = "PATCH";
	public static final String DELETE = "DELETE";
	public static final String OPTIONS = "OPTIONS";
	public static final String TRACE = "TRACE";

	/**
	 * 用户类型 配置
	 */
	public static final String MEMBER_USER = "member";
	public static final String BACKEND_USER = "backend";

	/**
	 * 登录类型 配置
	 */
	public static final String PHONE_LOGIN = "phone";
	public static final String QR_LOGIN = "qr";
	public static final String PASSWORD_LOGIN = "password";

	/**
	 * 资源 配置
	 */
	public static final String TAOTAO_CLOUD_SERVICE_RESOURCE = "taotao-cloud-service-resource";
	public static final Long TAOTAO_CLOUD_RESOURCE_EXPIRE = 18000L;
	public static final String TAOTAO_CLOUD_API_RESOURCE = "taotao-cloud-api-resource";

	/**
	 * token信息 配置
	 */
	public static final String TAOTAO_CLOUD_TOKEN_HEADER = "Authorization";
	public static final String TAOTAO_CLOUD_TOKEN_BEARER_TYPE = "Bearer";
	public static final String TAOTAO_CLOUD_ACCESS_TOKEN = "access_token";
	public static final String TAOTAO_CLOUD_GRANT_TYPE = "grant_type";
	public static final String TAOTAO_CLOUD_USER_TYPE = "user_type";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	public static final String CUSTOM_OAUTH_LOGIN = "/oauth/login";
	public static final String PHONE = "phone";
	public static final String VERIFY_CODE = "verify_code";
	public static final String QR_CODE = "qr_code";

	/**
	 * 邮箱
	 */
	public static final String RESET_MAIL = "MAIL";

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
	 * 注册中心元数据 版本号
	 */
	public static final String METADATA_VERSION = "version";

	/**
	 * 邮箱
	 */
	public static final Long MENU_TREE_ROOT_ID = 0L;

	public static final String ZIPKIN_TRACE_ID = "X-B3-TraceId";
	public static final String ZIPKIN_SPANE_ID = "X-B3-SpanId";

	//免费图床
	public static final String SM_MS_URL = "https://sm.ms/api";

	// IP归属地查询
	public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

}
