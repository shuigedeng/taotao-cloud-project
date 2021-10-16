/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.uc.api.service;

import com.taotao.cloud.uc.api.dto.user.RestPasswordUserDTO;
import com.taotao.cloud.uc.api.entity.SysUser;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;
import java.util.Set;

/**
 * ISysUserService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:48:28
 */
public interface ISysUserService<T extends SuperEntity<I>, I extends Serializable> extends
	BaseSuperService<T, I> {

	SysUser saveUser(SysUser sysUser);
	SysUser updateUser(SysUser sysUser);
	/**
	 * 重置密码
	 *
	 * @param id              id
	 * @param restPasswordDTO restPasswordDTO
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:49:02
	 */
	Boolean restPass(Long userId, RestPasswordUserDTO restPasswordDTO);

	/**
	 * 更新用户角色信息
	 *
	 * @param userId  userId
	 * @param roleIds roleIds
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:49:19
	 */
	Boolean updateUserRoles(Long userId, Set<Long> roleIds);

	/**
	 * 根据手机号码查询用户是否存在
	 *
	 * @param phone phone
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:49:35
	 */
	Boolean existsByPhone(String phone);

	/**
	 * 根据用户id查询用户是否存在
	 *
	 * @param id id
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:49:40
	 */
	Boolean existsById(Long id);
}
