/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.taotao.cloud.auth.biz.authentication.login.extension.oneClick;

import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.JustAuthUserDetailsRegisterService;
import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service.OneClickLoginService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
