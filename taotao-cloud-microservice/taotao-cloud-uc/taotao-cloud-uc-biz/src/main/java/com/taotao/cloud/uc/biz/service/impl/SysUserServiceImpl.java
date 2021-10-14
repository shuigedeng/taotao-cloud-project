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
package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.api.service.ISysUserRoleService;
import com.taotao.cloud.uc.api.service.ISysUserService;
import com.taotao.cloud.uc.biz.entity.SysUser;
import com.taotao.cloud.uc.biz.entity.SysUserRole;
import com.taotao.cloud.uc.biz.mapper.SysUserMapper;
import com.taotao.cloud.uc.biz.mapper.SysUserRoleMapper;
import com.taotao.cloud.uc.biz.repository.SysUserRepository;
import com.taotao.cloud.uc.biz.repository.SysUserRoleRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * SysUserServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:50:41
 */
@Service
public class SysUserServiceImpl extends
	BaseSuperServiceImpl<SysUserMapper, SysUser, SysUserRepository, Long>
	implements ISysUserService<SysUser, Long> {

//	private final static QSysUser SYS_USER = QSysUser.sysUser;
//
//	private final static String DEFAULT_PASSWORD = "123456";
//	private final static String DEFAULT_USERNAME = "admin";
//
//	private final SysUserRepository sysUserRepository;
//	private final ISysUserRoleService sysUserRoleService;
//
//	public SysUserServiceImpl(SysUserRepository sysUserRepository,
//		ISysUserRoleService sysUserRoleService) {
//		this.sysUserRepository = sysUserRepository;
//		this.sysUserRoleService = sysUserRoleService;
//	}
//
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public SysUser saveUser(SysUser sysUser) {
//		if (Objects.nonNull(sysUser.getId())) {
//			throw new BusinessException("不允许存在id值");
//		}
//		String phone = sysUser.getPhone();
//		Boolean isExists = existsByPhone(phone);
//		if (isExists) {
//			throw new BusinessException(ResultEnum.USER_PHONE_EXISTS_ERROR);
//		}
//		String nickname = sysUser.getNickname();
//		if (StrUtil.isBlank(nickname)) {
//			sysUser.setNickname(DEFAULT_USERNAME);
//		}
//		String username = sysUser.getUsername();
//		if (StrUtil.isBlank(username)) {
//			sysUser.setUsername(DEFAULT_USERNAME);
//		}
//
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		sysUser.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
//		return sysUserRepository.save(sysUser);
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public SysUser updateUser(SysUser sysUser) {
//		if (Objects.isNull(sysUser.getId())) {
//			throw new BusinessException("id不能为空");
//		}
//		return sysUserRepository.save(sysUser);
//
//		// 此处修改用户角色
//		// userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getId, sysUser.getId()));
//		// List<SysUserRole> userRoles = userAddDto.getRoleList().stream().map(item -> {
//		//     SysUserRole sysUserRole = new SysUserRole();
//		//     sysUserRole.setRoleId(item);
//		//     sysUserRole.setUserId(sysUser.getId());
//		//     return sysUserRole;
//		// }).collect(Collectors.toList());
//		//
//		// return userRoleService.saveBatch(userRoles);
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean removeUser(Long id) {
//		sysUserRepository.deleteById(id);
//		return true;
//	}
//
//	@Override
//	public Page<SysUser> findUserPage(Pageable page, UserPageQuery userQuery) {
//		BooleanExpression predicate = SYS_USER.delFlag.eq(false);
//		Optional.ofNullable(userQuery.getNickname())
//			.ifPresent(nickname -> predicate.and(SYS_USER.nickname.like(nickname)));
//		Optional.ofNullable(userQuery.getUsername())
//			.ifPresent(username -> predicate.and(SYS_USER.username.like(username)));
//		Optional.ofNullable(userQuery.getPhone())
//			.ifPresent(phone -> predicate.and(SYS_USER.phone.eq(phone)));
//		Optional.ofNullable(userQuery.getEmail())
//			.ifPresent(email -> predicate.and(SYS_USER.email.eq(email)));
//		Optional.ofNullable(userQuery.getDeptId())
//			.ifPresent(deptId -> predicate.and(SYS_USER.deptId.eq(deptId)));
//		Optional.ofNullable(userQuery.getJobId())
//			.ifPresent(jobId -> predicate.and(SYS_USER.jobId.eq(jobId)));
//
//		OrderSpecifier<LocalDateTime> createDatetimeDesc = SYS_USER.createTime.desc();
//		return sysUserRepository.findPageable(predicate, page, createDatetimeDesc);
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean restPass(Long id, RestPasswordUserDTO restPasswordDTO) {
//		String restPasswordPhone = restPasswordDTO.getPhone();
//		SysUser sysUser = findUserInfoById(id);
//
//		String phone = sysUser.getPhone();
//		if (!Objects.equals(restPasswordPhone, phone)) {
//			throw new BusinessException(ResultEnum.USER_PHONE_INCONSISTENT_ERROR);
//		}
////		BCryptPasswordEncoder passwordEncoder = AuthUtil.getPasswordEncoder();
////		BCryptPasswordEncoder passwordEncoder = AuthUtil.getPasswordEncoder();
////
////		String oldPassword = restPasswordDTO.getOldPassword();
////		String password = sysUser.getPassword();
////		if (!AuthUtil.validatePass(oldPassword, password)) {
////			throw new BusinessException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
////		}
//
//		String newPassword = restPasswordDTO.getNewPassword();
////		return sysUserRepository.updatePassword(id, passwordEncoder.encode(newPassword));
//		return true;
//	}
//
//	@Override
//	public SysUser findUserInfoById(Long userId) {
//		Optional<SysUser> optionalSysUser = sysUserRepository.findById(userId);
//		return optionalSysUser.orElseThrow(() -> new BusinessException(ResultEnum.USER_NOT_EXIST));
//	}
//
//	@Override
//	public SysUser findUserInfoByUsername(String username) {
//		BooleanExpression expression = SYS_USER.username.eq(username);
//		return sysUserRepository.fetchOne(expression);
//	}
//
//	@Override
//	public Boolean existsByPhone(String phone) {
//		BooleanExpression phonePredicate = SYS_USER.phone.eq(phone);
//		return sysUserRepository.exists(phonePredicate);
//	}
//
//	@Override
//	public Boolean existsById(Long id) {
//		BooleanExpression phonePredicate = SYS_USER.id.eq(id);
//		return sysUserRepository.exists(phonePredicate);
//	}
//
//	@Override
//	public List<SysUser> findUserList(UserQuery userQuery) {
//		BooleanExpression predicate = SYS_USER.delFlag.eq(false);
//		Optional.ofNullable(userQuery.getNickname())
//			.ifPresent(nickname -> predicate.and(SYS_USER.nickname.like(nickname)));
//		Optional.ofNullable(userQuery.getUsername())
//			.ifPresent(username -> predicate.and(SYS_USER.username.like(username)));
//		Optional.ofNullable(userQuery.getPhone())
//			.ifPresent(phone -> predicate.and(SYS_USER.phone.eq(phone)));
//		Optional.ofNullable(userQuery.getEmail())
//			.ifPresent(email -> predicate.and(SYS_USER.email.eq(email)));
//		Optional.ofNullable(userQuery.getType())
//			.ifPresent(type -> predicate.and(SYS_USER.type.eq(type)));
//		Optional.ofNullable(userQuery.getSex())
//			.ifPresent(sex -> predicate.and(SYS_USER.sex.eq(sex)));
//		Optional.ofNullable(userQuery.getDeptId())
//			.ifPresent(deptId -> predicate.and(SYS_USER.deptId.eq(deptId)));
//		Optional.ofNullable(userQuery.getJobId())
//			.ifPresent(jobId -> predicate.and(SYS_USER.jobId.eq(jobId)));
//		return sysUserRepository.fetch(predicate);
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean updateUserRoles(UserRoleDTO userRoleDTO) {
//		Long userId = userRoleDTO.getUserId();
//		findUserInfoById(userId);
//		return sysUserRoleService.saveUserRoles(userId, userRoleDTO.getRoleIds());
//	}
//
////    @Override
////    public boolean doPostSignUp(SysUser user) {
//// 进行账号校验
////        SysUser sysUser = findSecurityUserByUser(new SysUser().setUsername(user.getUsername()));
////        if (ObjectUtil.isNull(sysUser)) {
////            throw new BaseException("账号不存在");
////        }
////        Integer userId = sysUser.getId();
////        try {
////            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername()去验证用户名和密码，
////            // 如果正确，则存储该用户名密码到security 的 context中
////            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
////        } catch (Exception e) {
////            if (e instanceof BadCredentialsException) {
////                throw new BaseException("用户名或密码错误", 402);
////            } else if (e instanceof DisabledException) {
////                throw new BaseException("账户被禁用", 402);
////            } else if (e instanceof AccountExpiredException) {
////                throw new BaseException("账户过期无法验证", 402);
////            } else {
////                throw new BaseException("账户被锁定,无法登录", 402);
////            }
////        }
////        //将业务系统的用户与社交用户绑定
////        socialRedisHelper.doPostSignUp(user.getKey(), userId);
////        return true;
////    }

}
