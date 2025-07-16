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

package com.taotao.cloud.auth.biz.strategy.local;

import com.taotao.boot.security.spring.core.AccessPrincipal;
import com.taotao.boot.security.spring.core.userdetails.TtcUser;
import com.taotao.cloud.auth.biz.strategy.AbstractStrategyUserDetailsService;
import com.taotao.cloud.auth.biz.strategy.user.SysUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>UserDetail本地直联服务 </p>
 */
public class LocalUserDetailsService extends AbstractStrategyUserDetailsService {

    private final SysUserService sysUserService;
    private final SocialAuthenticationHandler socialAuthenticationHandler;

    public LocalUserDetailsService(
            SysUserService sysUserService,
            SocialAuthenticationHandler socialAuthenticationHandler) {
        this.sysUserService = sysUserService;
        this.socialAuthenticationHandler = socialAuthenticationHandler;
    }

    @Override
    public TtcUser findUserDetailsByUsername(String userName) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.findByUserName(userName);
        return this.convertSysUser(sysUser, userName);
    }

    @Override
    public TtcUser findUserDetailsBySocial(String source, AccessPrincipal accessPrincipal) {
        return socialAuthenticationHandler.authentication(source, accessPrincipal);
    }
}
