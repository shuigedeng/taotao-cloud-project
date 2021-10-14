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

import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;

/**
 * ISysUserService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:48:28
 */
public interface ISysUserService<T extends SuperEntity<I>, I extends Serializable> extends
	BaseSuperService<T, I>  {

	///**
	// * 添加用户
	// *
	// * @param sysUser sysUser
	// * @return {@link SysUser }
	// * @author shuigedeng
	// * @since 2021-10-09 20:48:34
	// */
	//SysUser saveUser(SysUser sysUser);
	//
	///**
	// * 更新用户
	// *
	// * @param sysUser sysUser
	// * @return {@link SysUser }
	// * @author shuigedeng
	// * @since 2021-10-09 20:48:44
	// */
	//SysUser updateUser(SysUser sysUser);
	//
	///**
	// * 根据用户id删除用户
	// *
	// * @param id id
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:48:50
	// */
	//Boolean removeUser(Long id);
	//
	///**
	// * 查询用户集合
	// *
	// * @param page      page
	// * @param userQuery userQuery
	// * @return {@link Page&lt;com.taotao.cloud.uc.biz.entity.SysUser&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:48:56
	// */
	//Page<SysUser> findUserPage(Pageable page, UserPageQuery userQuery);
	//
	///**
	// * 重置密码
	// *
	// * @param id              id
	// * @param restPasswordDTO restPasswordDTO
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:49:02
	// */
	//Boolean restPass(Long id, RestPasswordUserDTO restPasswordDTO);
	//
	///**
	// * 根据用户id查询用户信息
	// *
	// * @param userId userId
	// * @return {@link SysUser }
	// * @author shuigedeng
	// * @since 2021-10-09 20:49:07
	// */
	//SysUser findUserInfoById(Long userId);
	//
	///**
	// * 查询用户集合
	// *
	// * @param userQuery userQuery
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysUser&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:49:13
	// */
	//List<SysUser> findUserList(UserQuery userQuery);
	//
	///**
	// * 更新角色信息
	// *
	// * @param userRoleDTO userRoleDTO
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:49:19
	// */
	//Boolean updateUserRoles(UserRoleDTO userRoleDTO);
	//
	///**
	// * 根据username获取用户信息
	// *
	// * @param username username
	// * @return {@link SysUser }
	// * @author shuigedeng
	// * @since 2021-10-09 20:49:25
	// */
	//SysUser findUserInfoByUsername(String username);
	//
	///**
	// * 根据手机号码查询用户是否存在
	// *
	// * @param phone phone
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:49:35
	// */
	//Boolean existsByPhone(String phone);
	//
	///**
	// * 根据用户id查询用户是否存在
	// *
	// * @param id id
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:49:40
	// */
	//Boolean existsById(Long id);
}
