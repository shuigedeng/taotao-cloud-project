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

package com.taotao.cloud.auth.biz.authentication.form.phone.service;

import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.security.springsecurity.core.definition.domain.HerodotusGrantedAuthority;
import com.taotao.cloud.security.springsecurity.core.definition.domain.HerodotusUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class DefaultOauth2FormOauth2FormPhoneUserDetailsService implements Oauth2FormPhoneUserDetailsService {

    @Override
    public UserDetails loadUserByPhone(String phone, String type) throws UsernameNotFoundException {

		Collection<HerodotusGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new HerodotusGrantedAuthority("manager.book.read1111"));
		authorities.add(new HerodotusGrantedAuthority("manager.book.write1111"));
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_A1");
		roles.add("ROLE_A2");
		// admin/123456
		HerodotusUser user = new HerodotusUser("33e781c5-31e0-4ea4-8b02-1236bde9643",
			"admin",
			"{bcrypt}$2a$10$lvjys/FAHAVmgXM.U1LtOOJ./C5SstExZCZ0Z5N7SeGZAue0JFtXC",
			true, true, true, true,
			authorities, roles, "", "");
		return user;
    }
}