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

package com.taotao.cloud.auth.biz.demo.core.jackson2;

import cn.herodotus.engine.oauth2.core.definition.details.FormLoginWebAuthenticationDetails;
import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusGrantedAuthority;
import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusUser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * Description: 自定义 User Details Module
 *
 * @author : gengwei.zheng
 * @date : 2022/2/17 23:39
 */
public class HerodotusJackson2Module extends SimpleModule {

    public HerodotusJackson2Module() {
        super(HerodotusJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
        context.setMixInAnnotations(HerodotusUser.class, HerodotusUserMixin.class);
        context.setMixInAnnotations(
                HerodotusGrantedAuthority.class, HerodotusGrantedAuthorityMixin.class);
        context.setMixInAnnotations(
                FormLoginWebAuthenticationDetails.class,
                FormLoginWebAuthenticationDetailsMixin.class);
    }
}
