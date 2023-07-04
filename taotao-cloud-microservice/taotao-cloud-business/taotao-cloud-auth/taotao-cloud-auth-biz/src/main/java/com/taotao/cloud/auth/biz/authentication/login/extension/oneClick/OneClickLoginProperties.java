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

package com.taotao.cloud.auth.biz.authentication.login.extension.oneClick;

import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.JustAuthUserDetailsRegisterService;
import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service.OneClickLoginService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 一键登录属性
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "taotao.cloud.auth.login.one-click-login")
public class OneClickLoginProperties {
    /**
     * 默认的一键登录请求处理url
     */
    public static final String DEFAULT_ONE_CLICK_LOGIN_PROCESSING_URL = "/authentication/one-click";

    /**
     * 一键登录是否开启, 默认 false
     */
    private Boolean enable = false;

    /**
     * 一键登录请求处理 url, 默认 /authentication/one-click
     */
    private String loginProcessingUrl = DEFAULT_ONE_CLICK_LOGIN_PROCESSING_URL;

    /**
     * token 参数名称, 默认: accessToken
     */
    private String tokenParamName = "accessToken";

    /**
     * 其他请求参数名称列表(包括请求头名称), 此参数会传递到 {@link OneClickLoginService#callback(String, Map)} 与
     * {@link JustAuthUserDetailsRegisterService#registerUser(String, Map)}; 默认为: 空
     */
    private List<String> otherParamNames = new ArrayList<>();
}
