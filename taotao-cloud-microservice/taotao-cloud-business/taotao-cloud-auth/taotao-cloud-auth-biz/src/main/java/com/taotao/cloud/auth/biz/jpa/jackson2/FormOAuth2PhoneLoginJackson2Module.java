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

package com.taotao.cloud.auth.biz.jpa.jackson2;

import tools.jackson.core.Version;
import tools.jackson.databind.module.SimpleModule;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * Jackson module for spring-security-core. This module register
 * {@link AnonymousAuthenticationTokenMixin}, {@link RememberMeAuthenticationTokenMixin},
 * {@link SimpleGrantedAuthorityMixin}, , {@link UserMixin}
 * and {@link FormOAuth2PhoneAuthenticationTokenMixin}. If no default typing enabled by
 * default then it'll enable it because typing info is needed to properly
 * serialize/deserialize objects. In order to use this module just add this module into
 * your JsonMapper configuration.
 *
 * <pre>
 *     JsonMapper mapper = new JsonMapper();
 *     mapper.registerModule(new CoreJackson2Module());
 * </pre> <b>Note: use {@link SecurityJackson2Modules#getModules(ClassLoader)} to get list
 * of all security modules.</b>
 *
 * @author Jitendra Singh.
 * @see SecurityJackson2Modules
 * @since 4.2
 */
@SuppressWarnings("serial")
public class FormOAuth2PhoneLoginJackson2Module extends SimpleModule {

    public FormOAuth2PhoneLoginJackson2Module() {
        super(
                FormOAuth2PhoneLoginJackson2Module.class.getName(),
                new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(
                Oauth2FormSmsLoginAuthenticationToken.class,
                FormOAuth2PhoneAuthenticationTokenMixin.class);
    }
}
