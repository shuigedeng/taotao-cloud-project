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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.constants;


import com.taotao.boot.security.spring.constants.BaseConstants;

/**
 * <p>接入模块常量 </p>
 *
 *
 * @since : 2022/1/25 15:27
 */
public interface AccessConstants extends BaseConstants {

    String PROPERTY_ACCESS_JUSTAUTH = PROPERTY_PREFIX_ACCESS + ".justauth";
    String ITEM_JUSTAUTH_ENABLED = PROPERTY_ACCESS_JUSTAUTH + PROPERTY_ENABLED;
    String PROPERTY_ACCESS_WXAPP = PROPERTY_PREFIX_ACCESS + ".wxapp";
    String ITEM_WXAPP_ENABLED = PROPERTY_ACCESS_WXAPP + PROPERTY_ENABLED;
    String PROPERTY_ACCESS_WXMPP = PROPERTY_PREFIX_ACCESS + ".wxmpp";
    String ITEM_WXMPP_ENABLED = PROPERTY_ACCESS_WXMPP + PROPERTY_ENABLED;

    String CACHE_NAME_TOKEN_JUSTAUTH = CACHE_TOKEN_BASE_PREFIX + "justauth:";
}
