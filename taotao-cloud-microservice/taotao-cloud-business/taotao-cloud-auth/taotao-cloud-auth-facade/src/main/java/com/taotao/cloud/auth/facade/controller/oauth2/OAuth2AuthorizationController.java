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

package com.taotao.cloud.auth.facade.controller.oauth2;

import com.taotao.cloud.auth.application.service.impl.TtcAuthorizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>OAuth2 认证管理接口 </p>
 *
 *
 * @since : 2022/3/1 18:52
 */
@RestController
@RequestMapping("/authorize/authorization")
@Tags({@Tag(name = "OAuth2 认证服务接口"), @Tag(name = "OAuth2 认证管理接口")})
public class OAuth2AuthorizationController {

    private final TtcAuthorizationService ttcAuthorizationService;

    @Autowired
    public OAuth2AuthorizationController(TtcAuthorizationService ttcAuthorizationService) {
        this.ttcAuthorizationService = ttcAuthorizationService;
    }

    //    @Override
    //    public WriteableService<TtcAuthorization, String> getWriteableService() {
    //        return this.ttcAuthorizationService;
    //    }
}
