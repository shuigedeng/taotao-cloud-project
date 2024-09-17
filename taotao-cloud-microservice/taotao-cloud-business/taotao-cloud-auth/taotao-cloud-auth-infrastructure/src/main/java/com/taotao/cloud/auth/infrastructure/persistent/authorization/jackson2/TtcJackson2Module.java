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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.jackson2;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.taotao.boot.security.spring.authentication.details.FormLoginWebAuthenticationDetails;
import com.taotao.boot.security.spring.authority.TtcGrantedAuthority;
import com.taotao.boot.security.spring.userdetails.TtcUser;
import com.taotao.boot.security.spring.utils.Jackson2Constants;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * <p>自定义 User Details Module </p>
 *
 *
 * @since : 2022/2/17 23:39
 */
public class TtcJackson2Module extends SimpleModule {

    public TtcJackson2Module() {
        super(TtcJackson2Module.class.getName(), Jackson2Constants.VERSION);
    }

    @Override
    public void setupModule(SetupContext context) {
        SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
        context.setMixInAnnotations(TtcUser.class, TtcUserMixin.class);
        context.setMixInAnnotations(TtcGrantedAuthority.class, TtcGrantedAuthorityMixin.class);
        context.setMixInAnnotations(
                FormLoginWebAuthenticationDetails.class, FormLoginWebAuthenticationDetailsMixin.class);
    }
}
