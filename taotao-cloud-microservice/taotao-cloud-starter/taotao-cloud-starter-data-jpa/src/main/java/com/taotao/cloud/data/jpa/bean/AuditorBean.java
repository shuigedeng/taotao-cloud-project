/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.data.jpa.bean;

import com.taotao.cloud.common.model.SecurityUser;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取当前审计用户 主要用于CreatedBy LastModifiedBy 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:30:59
 */
public class AuditorBean implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		SecurityUser user;
		try {
			user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
			return Optional.ofNullable(user.getUserId());
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
