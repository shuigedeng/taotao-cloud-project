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

package com.taotao.cloud.auth.biz.management.processor;

import com.taotao.boot.security.spring.core.AccessPrincipal;
import com.taotao.boot.security.spring.core.userdetails.TtcUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>自定义UserDetailsService接口，方便以后扩展 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 10:05:41
 */
public interface EnhanceUserDetailsService extends UserDetailsService {

    /**
     * 通过社交集成的唯一id，获取用户信息
     * <p>
     * 如果是短信验证码，openId就是手机号码
     *
     * @param source          社交集成提供商类型
     * @param accessPrincipal 社交登录提供的相关信息
     * @return {@link UserDetails }
     * @since 2023-07-04 10:05:41
     */
    UserDetails loadUserBySocial(String source, AccessPrincipal accessPrincipal)
            throws AuthenticationException;

    /**
     * 系统用户名
     *
     * @param username 用户账号
     * @return {@link TtcUser }
     * @since 2023-07-04 10:05:41
     */
    TtcUser loadTtcUserByUsername(String username) throws UsernameNotFoundException;
}
