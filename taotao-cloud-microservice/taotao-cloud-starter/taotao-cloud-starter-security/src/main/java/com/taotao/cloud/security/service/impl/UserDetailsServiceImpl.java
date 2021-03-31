///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.security.service.impl;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.taotao.cloud.common.constant.CommonConstant;
//import com.taotao.cloud.core.utils.BeanUtil;
//import com.taotao.cloud.common.utils.LogUtil;
//import com.taotao.cloud.core.model.Result;
//import com.taotao.cloud.core.model.SecurityUser;
//import com.taotao.cloud.member.api.feign.RemoteMemberService;
//import com.taotao.cloud.security.service.IUserDetailsService;
//import com.taotao.cloud.uc.api.feign.RemoteResourceService;
//import com.taotao.cloud.uc.api.feign.RemoteRoleService;
//import com.taotao.cloud.uc.api.feign.RemoteUserService;
//import com.taotao.cloud.uc.api.vo.resource.ResourceVO;
//import com.taotao.cloud.uc.api.vo.role.RoleVO;
//import com.taotao.cloud.uc.api.vo.user.UserVO;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * UserServiceDetail
// *
// * @author dengtao
// * @since 2020/4/29 17:46
// */
//public class UserDetailsServiceImpl implements IUserDetailsService {
//
//	@Resource
//	private RemoteUserService remoteUserService;
//	@Resource
//	private RemoteMemberService remoteMemberService;
//	@Resource
//	private RemoteRoleService remoteRoleService;
//	@Resource
//	private RemoteResourceService remoteResourceService;
//
//	/**
//	 * 通过用户名查询用户
//	 *
//	 * @param username 用户名
//	 * @return org.springframework.security.core.userdetails.UserDetails
//	 * @author dengtao
//	 * @since 2020/4/29 17:46
//	 */
//	@Override
//	public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
//		// 查询用户信息
//		Result<UserVO> userResult = remoteUserService.findUserInfoByUsername(username);
//		if (ObjectUtil.isNull(userResult) || Objects.isNull(userResult.getData())) {
//			LogUtil.error("后台用户 username:[" + username + "]不存在");
//			throw new UsernameNotFoundException("后台用户 username:[" + username + "]不存在");
//		}
//		UserVO userVO = userResult.getData();
//		Long userId = userVO.getId();
//		//查询角色信息
//		Result<List<RoleVO>> roleResult = remoteRoleService.findRoleByUserId(userId);
//		if (ObjectUtil.isNull(roleResult) || Objects.isNull(roleResult.getData())) {
//			LogUtil.error("后台用户 userId:[" + userId + "]角色查询失败");
//			throw new UsernameNotFoundException("会员用户 userId:[" + userId + "]角色查询失败");
//		}
//		List<RoleVO> roles = roleResult.getData();
//		Set<String> roleCodes = roles.stream().filter(Objects::nonNull).map(RoleVO::getCode)
//			.collect(Collectors.toSet());
//		//查询资源信息
//		Result<List<ResourceVO>> resourceResult = remoteResourceService
//			.findResourceByCodes(roleCodes);
//		if (ObjectUtil.isNull(roleResult) || Objects.isNull(roleResult.getData())) {
//			LogUtil.error("后台用户 userId:[" + userId + "]资源查询失败");
//			throw new UsernameNotFoundException("会员用户 userId:[" + userId + "]资源查询失败");
//		}
//		List<ResourceVO> resources = resourceResult.getData();
//		Set<String> perms = resources.stream().filter(Objects::nonNull)
//			.filter(resource -> StrUtil.isNotBlank(resource.getPerms())).map(ResourceVO::getPerms)
//			.collect(Collectors.toSet());
//
//		SecurityUser user = new SecurityUser();
//		BeanUtil.copyIncludeNull(userVO, user);
//		user.setUserId(userVO.getId());
//		user.setRoles(roleCodes);
//		user.setPermissions(perms);
//
//		return user;
//	}
//
//	@Override
//	public SecurityUser loadUserSecurityUser(String username, String userType, String loginType) {
//		if (CommonConstant.BACKEND_USER.equals(userType)) {
//			return loadUserByUsername(username);
//		} else {
//			/**
//			 * 1.扫码登陆根据key从redis查询用户
//			 * 2.手机验证码根据电话号码查询用户
//			 * 3.账号密码登陆根据用户名查询用户
//			 */
//			if (CommonConstant.QR_LOGIN.equals(loginType)) {
//				// 扫码登陆根据key从redis查询用户
//				return null;
//			} else {
//				Result<SecurityUser> memberSecurityUser = remoteMemberService
//					.getMemberSecurityUser(username);
//				if (ObjectUtil.isNull(memberSecurityUser)
//					|| memberSecurityUser.getCode() == HttpStatus.NOT_FOUND.value()) {
//					LogUtil.error("会员用户：[" + username + "]不存在");
//					throw new UsernameNotFoundException("会员用户：[" + username + "]不存在");
//				}
//				return memberSecurityUser.getData();
//			}
//		}
//	}
//}
//
