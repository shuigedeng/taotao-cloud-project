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
package com.taotao.cloud.sys.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.sys.api.web.dto.user.RestPasswordUserDTO;
import com.taotao.cloud.sys.api.dubbo.IDubboUserService;
import com.taotao.cloud.sys.biz.model.entity.system.QUser;
import com.taotao.cloud.sys.biz.model.entity.system.User;
import com.taotao.cloud.sys.biz.model.entity.system.UserRole;
import com.taotao.cloud.sys.biz.mapper.IUserMapper;
import com.taotao.cloud.sys.biz.repository.cls.UserRepository;
import com.taotao.cloud.sys.biz.repository.inf.IUserRepository;
import com.taotao.cloud.sys.biz.service.IUserRoleService;
import com.taotao.cloud.sys.biz.service.IUserService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:50:41
 */
@Service
@DubboService(interfaceClass = IDubboUserService.class, validation = "true")
public class UserServiceImpl extends
	BaseSuperServiceImpl<IUserMapper, User, UserRepository, IUserRepository, Long>
	implements IDubboUserService, IUserService {

	private final static QUser USER = QUser.user;

	private final static String DEFAULT_PASSWORD = "123456";
	private final static String DEFAULT_USERNAME = "admin";

	private final IUserRoleService userRoleService;

	public UserServiceImpl(IUserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User saveUser(User sysUser) {
		if (Objects.nonNull(sysUser.getId())) {
			throw new BusinessException("不允许存在id值");
		}
		String phone = sysUser.getPhone();
		Boolean isExists = existsByPhone(phone);
		if (isExists) {
			throw new BusinessException(ResultEnum.USER_PHONE_EXISTS_ERROR);
		}
		String nickname = sysUser.getNickname();
		if (StrUtil.isBlank(nickname)) {
			sysUser.setNickname(DEFAULT_USERNAME);
		}
		String username = sysUser.getUsername();
		if (StrUtil.isBlank(username)) {
			sysUser.setUsername(DEFAULT_USERNAME);
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		sysUser.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
		return ir().save(sysUser);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User updateUser(User user) {
		if (Objects.isNull(user.getId())) {
			throw new BusinessException("id不能为空");
		}

		//此处修改用户角色
		userRoleService.remove(
			Wrappers.<UserRole>lambdaQuery().eq(UserRole::getId, user.getId()));
		List<UserRole> userRoles = new ArrayList<Long>().stream()
			.map(item -> {
				UserRole sysUserRole = new UserRole();
				sysUserRole.setRoleId(item);
				sysUserRole.setUserId(user.getId());
				return sysUserRole;
			}).collect(Collectors.toList());

		userRoleService.saveBatch(userRoles);
		return user;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean restPass(Long userId, RestPasswordUserDTO restPasswordDTO) {
		String restPasswordPhone = restPasswordDTO.getPhone();
		User sysUser = getById(userId);

		//String phone = sysUser.getPhone();
		//if (!Objects.equals(restPasswordPhone, phone)) {
		//	throw new BusinessException(ResultEnum.USER_PHONE_INCONSISTENT_ERROR);
		//}
		//BCryptPasswordEncoder passwordEncoder = AuthUtil.getPasswordEncoder();
		//BCryptPasswordEncoder passwordEncoder = AuthUtil.getPasswordEncoder();
		//
		//String oldPassword = restPasswordDTO.getOldPassword();
		//String password = sysUser.getPassword();
		//if (!AuthUtil.validatePass(oldPassword, password)) {
		//	throw new BusinessException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
		//}
		//
		//String newPassword = restPasswordDTO.getNewPassword();
		//return sysUserRepository.updatePassword(id, passwordEncoder.encode(newPassword));
		return true;
	}


	@Override
	public Boolean existsByPhone(String phone) {
		BooleanExpression phonePredicate = USER.phone.eq(phone);
		return cr().exists(phonePredicate);
	}

	@Override
	public Boolean existsById(Long id) {
		BooleanExpression phonePredicate = USER.id.eq(id);
		return cr().exists(phonePredicate);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateUserRoles(Long userId, Set<Long> roleIds) {
		return userRoleService.saveUserRoles(userId, roleIds);
	}

	//@Override
	//public boolean doPostSignUp(User user) {
	//    //进行账号校验
	//    User sysUser = findSecurityUserByUser(new User().setUsername(user.getUsername()));
	//    if (ObjectUtil.isNull(sysUser)) {
	//        throw new BaseException("账号不存在");
	//    }
	//    Integer userId = sysUser.getId();
	//    try {
	//        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername()去验证用户名和密码，
	//        // 如果正确，则存储该用户名密码到security 的 context中
	//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	//    } catch (Exception e) {
	//        if (e instanceof BadCredentialsException) {
	//            throw new BaseException("用户名或密码错误", 402);
	//        } else if (e instanceof DisabledException) {
	//            throw new BaseException("账户被禁用", 402);
	//        } else if (e instanceof AccountExpiredException) {
	//            throw new BaseException("账户过期无法验证", 402);
	//        } else {
	//            throw new BaseException("账户被锁定,无法登录", 402);
	//        }
	//    }
	//    //将业务系统的用户与社交用户绑定
	//    socialRedisHelper.doPostSignUp(user.getKey(), userId);
	//    return true;
	//}

}
