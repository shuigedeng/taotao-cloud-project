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

import static com.taotao.cloud.auth.biz.authentication.federation.strategy.impl.GiteeUserConverter.THIRD_LOGIN_GITEE;

import com.taotao.cloud.auth.biz.authentication.federation.Oauth2ThirdAccount;
import com.taotao.cloud.auth.biz.authentication.federation.strategy.Oauth2UserConverterStrategy;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * 转换通过码云登录的用户信息
 */
@Component(THIRD_LOGIN_GITEE)
public class GiteeUserConverter implements Oauth2UserConverterStrategy {
    /**
     * 三方登录类型——Gitee
     */
    public static final String THIRD_LOGIN_GITEE = "gitee";

    @Override
    public Oauth2ThirdAccount convert(OAuth2User oAuth2User) {
        // 获取三方用户信息
        Map<String, Object> attributes = oAuth2User.getAttributes();
        // 转换至Oauth2ThirdAccount
        Oauth2ThirdAccount thirdAccount = new Oauth2ThirdAccount();
        thirdAccount.setUniqueId(oAuth2User.getName());
        thirdAccount.setThirdUsername(String.valueOf(attributes.get("login")));
        thirdAccount.setType(THIRD_LOGIN_GITEE);
        thirdAccount.setBlog(String.valueOf(attributes.get("blog")));
        // 设置基础用户信息
        thirdAccount.setName(String.valueOf(attributes.get("name")));
        thirdAccount.setAvatarUrl(String.valueOf(attributes.get("avatar_url")));
        return thirdAccount;
    }
}
