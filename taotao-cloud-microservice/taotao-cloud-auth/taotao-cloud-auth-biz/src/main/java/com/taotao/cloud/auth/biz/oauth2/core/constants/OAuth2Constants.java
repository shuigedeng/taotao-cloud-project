/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.core.constants;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;

/**
 * <p>Description: OAuth2 模块通用常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 9:44
 */
public interface OAuth2Constants extends BaseConstants {

    String PROPERTY_PREFIX_OAUTH2 = PROPERTY_PREFIX_HERODOTUS + ".oauth2";
    String PROPERTY_OAUTH2_UI = PROPERTY_PREFIX_OAUTH2 + ".ui";
    String PROPERTY_OAUTH2_COMPLIANCE = PROPERTY_PREFIX_OAUTH2 + ".compliance";
    String ITEM_COMPLIANCE_AUTO_UNLOCK = PROPERTY_OAUTH2_COMPLIANCE + ".auto-unlock";

    String REGION_OAUTH2_AUTHORIZATION = AREA_PREFIX + "oauth2:authorization";
    String REGION_OAUTH2_AUTHORIZATION_CONSENT = AREA_PREFIX + "oauth2:authorization:consent";
    String REGION_OAUTH2_REGISTERED_CLIENT = AREA_PREFIX + "oauth2:registered:client";
    String REGION_OAUTH2_APPLICATION = AREA_PREFIX + "oauth2:application";
    String REGION_OAUTH2_COMPLIANCE = AREA_PREFIX + "oauth2:compliance";
    String REGION_OAUTH2_AUTHORITY = AREA_PREFIX + "oauth2:authority";
    String REGION_OAUTH2_SCOPE = AREA_PREFIX + "oauth2:scope";
    String REGION_OAUTH2_APPLICATION_SCOPE = AREA_PREFIX + "oauth2:application:scope";

    String CACHE_NAME_TOKEN_SIGN_IN_FAILURE_LIMITED = CACHE_TOKEN_BASE_PREFIX + "sign_in:failure_limited:";
    String CACHE_NAME_TOKEN_LOCKED_USER_DETAIL = CACHE_TOKEN_BASE_PREFIX + "locked:user_details:";
}
