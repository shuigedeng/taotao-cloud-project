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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.deserializes;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.JustAuthAuthenticationToken;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.userdetails.TemporaryUser;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Auth2 Jackson2 Module
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/28 10:58
 */
public class Auth2Jackson2Module extends SimpleModule {

    public Auth2Jackson2Module() {
        super(Auth2Jackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
        context.setMixInAnnotations(
                JustAuthAuthenticationToken.class,
                Auth2AuthenticationTokenJsonDeserializer.Auth2AuthenticationTokenMixin.class);
        context.setMixInAnnotations(
                RememberMeAuthenticationToken.class,
                RememberMeAuthenticationTokenJsonDeserializer.RememberMeAuthenticationTokenMixin.class);
        context.setMixInAnnotations(
                AnonymousAuthenticationToken.class,
                AnonymousAuthenticationTokenJsonDeserializer.AnonymousAuthenticationTokenMixin.class);
        context.setMixInAnnotations(User.class, UserDeserializer.UserMixin.class);
        context.setMixInAnnotations(TemporaryUser.class, TemporaryUserDeserializer.TemporaryUserMixin.class);
        context.setMixInAnnotations(
                WebAuthenticationDetails.class,
                WebAuthenticationDetailsDeserializer.WebAuthenticationDetailsMixin.class);
        context.setMixInAnnotations(AuthUser.class, AuthUserJsonDeserializer.AuthUserMixin.class);
        context.setMixInAnnotations(AuthToken.class, AuthUserJsonDeserializer.AuthTokenMixin.class);
    }
}
