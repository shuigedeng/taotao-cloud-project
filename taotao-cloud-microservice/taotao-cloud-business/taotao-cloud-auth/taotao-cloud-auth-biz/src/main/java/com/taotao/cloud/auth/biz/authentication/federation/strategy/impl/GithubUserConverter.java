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

package com.taotao.cloud.auth.biz.authentication.federation.strategy.impl;

import static com.taotao.cloud.auth.biz.authentication.federation.strategy.impl.GithubUserConverter.THIRD_LOGIN_GITHUB;

import com.taotao.cloud.auth.biz.authentication.federation.Oauth2ThirdAccount;
import com.taotao.cloud.auth.biz.authentication.federation.strategy.Oauth2UserConverterStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * 转换通过Github登录的用户信息
 */
@RequiredArgsConstructor
@Component(THIRD_LOGIN_GITHUB)
public class GithubUserConverter implements Oauth2UserConverterStrategy {
    /**
     * 三方登录类型——Github
     */
    public static final String THIRD_LOGIN_GITHUB = "github";

    private final GiteeUserConverter userConverter;

    protected static final String LOGIN_TYPE = THIRD_LOGIN_GITHUB;

    @Override
    public Oauth2ThirdAccount convert(OAuth2User oAuth2User) {
        // github与gitee目前所取字段一致，直接调用gitee的解析
        Oauth2ThirdAccount thirdAccount = userConverter.convert(oAuth2User);
        // 提取location
        Object location = oAuth2User.getAttributes().get("location");
        thirdAccount.setLocation(String.valueOf(location));
        // 设置登录类型
        thirdAccount.setType(LOGIN_TYPE);
        return thirdAccount;
    }
}
