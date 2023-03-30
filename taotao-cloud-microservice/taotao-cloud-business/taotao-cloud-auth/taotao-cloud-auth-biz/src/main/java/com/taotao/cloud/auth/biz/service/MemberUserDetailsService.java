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

package com.taotao.cloud.auth.biz.service;

import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * CloudUserDetailsService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-20 16:20:24
 */
public class MemberUserDetailsService implements UserDetailsService {

    @Autowired private IFeignMemberApi memberApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Result<SecurityUser> memberSecurityUser = feignMemberService.getMemberSecurityUser(
        //	username);
        // if (!memberSecurityUser.success() || memberSecurityUser == null) {
        //	LogUtil.error("会员用户 [{}] not found.", username);
        //	throw new UsernameNotFoundException(String.format("会员用户 [%s] 不存在", username));
        // }

        Set<String> permissions = new HashSet<>();
        permissions.add("read");
        permissions.add("write");

        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("super_admin");

        // SecurityUser user = SecurityUser.builder()
        // 	.userId(1L)
        // 	.username("taotao")
        // 	.nickname("taotao")
        // 	// 123456
        // 	.password("$2a$10$FefUzIWJ4ukySUBNtSiw1ezdAutiHihXm4/xgrkhogr1.P0rJ18v2")
        // 	.phone("15730445331")
        // 	.mobile("15730445331")
        // 	.deptId("1")
        // 	.jobId("1")
        // 	.email("981376578@qq.com")
        // 	.sex(1)
        // 	.status(1)
        // 	.type(2)
        // 	.permissions(permissions)
        // 	.roles(roles)
        // 	.build();

        return new SecurityUser();
        // return memberSecurityUser;

    }
}
