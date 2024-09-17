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

package com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy;

import com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy.user.SysUser;
import com.taotao.cloud.auth.infrastructure.authentication.userdetails.strategy.user.UpmsHelper;
import com.taotao.boot.security.spring.userdetails.TtcUser;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>抽象StrategyUserDetailsService，提取公共方法 </p>
 *
 * @since : 2022/2/17 11:23
 */
public abstract class AbstractStrategyUserDetailsService implements StrategyUserDetailsService {

    protected TtcUser convertSysUser(SysUser sysUser, String userName) throws AuthenticationException {
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new UsernameNotFoundException("系统用户 " + userName + " 不存在!");
        }

        return UpmsHelper.convertSysUserToTtcUser(sysUser);
    }
}
