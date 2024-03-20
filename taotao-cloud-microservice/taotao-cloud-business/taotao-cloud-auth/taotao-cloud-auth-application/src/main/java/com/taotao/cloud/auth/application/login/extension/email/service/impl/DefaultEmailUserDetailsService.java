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

package com.taotao.cloud.auth.application.login.extension.email.service.impl;

import com.taotao.cloud.auth.application.login.extension.email.service.EmailUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmailUserDetailsService implements EmailUserDetailsService {

    //	@Autowired
    //	private SysUserMapper sysUserMapper;
    //
    //	@Autowired
    //	private SysRoleMapper sysRoleMapper;

    @Override
    public UserDetails loadUserByEmail(String email) {

        //		SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, email));
        //		if (Objects.isNull(user)) {
        //			return null;
        //		}
        //		// 获得用户角色信息
        //		List<String> roles = sysRoleMapper.selectByRoleId(user.getRoleId());
        //		// 构建 SimpleGrantedAuthority 对象
        //		List<SimpleGrantedAuthority> authorities =
        // roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        //		return new SysUserDetails(user, authorities);
        return null;
    }
}
