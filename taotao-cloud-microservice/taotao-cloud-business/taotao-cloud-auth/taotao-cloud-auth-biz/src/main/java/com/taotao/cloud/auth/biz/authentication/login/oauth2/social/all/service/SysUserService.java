/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.service;

import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.entity.SysUser;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.enums.AccountType;
import com.taotao.cloud.data.jpa.tenant.DataItemStatus;
import com.taotao.cloud.security.springsecurity.core.definition.domain.HerodotusUser;
import com.taotao.cloud.security.springsecurity.core.definition.domain.SocialUserDetails;
import com.taotao.cloud.security.springsecurity.core.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.data.id.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p>Description: SysUserService </p>
 *
 * 
 * @date : 2019/11/9 10:03
 */
@Service
public class SysUserService {

	private static final Logger log = LoggerFactory.getLogger(SysUserService.class);

//	private final SysUserRepository sysUserRepository;
//	private final SysDefaultRoleService sysDefaultRoleService;
//
//	public SysUserService(SysUserRepository sysUserRepository, SysDefaultRoleService sysDefaultRoleService) {
//		this.sysUserRepository = sysUserRepository;
//		this.sysDefaultRoleService = sysDefaultRoleService;
//	}


	public SysUser findByUserName(String userName) {
//		return sysUserRepository.findByUserName(userName);
		return null;
	}

	public SysUser findByUserId(String userId) {
//		return sysUserRepository.findByUserId(userId);
		return null;
	}

	public SysUser changePassword(String userId, String password) {
		SysUser sysUser = findByUserId(userId);
		sysUser.setPassword(SecurityUtils.encrypt(password));
//		return saveAndFlush(sysUser);
		return null;
	}

	public SysUser assign(String userId, String[] roleIds) {
		SysUser sysUser = findByUserId(userId);
		return this.register(sysUser, roleIds);
	}

	public SysUser register(SysUser sysUser, String[] roleIds) {
//		Set<SysRole> sysRoles = new HashSet<>();
//		for (String roleId : roleIds) {
//			SysRole sysRole = new SysRole();
//			sysRole.setRoleId(roleId);
//			sysRoles.add(sysRole);
//		}
//		return this.register(sysUser, sysRoles);
		return null;
	}

	public SysUser register(SysUser sysUser, AccountType source) {
//		SysDefaultRole sysDefaultRole = sysDefaultRoleService.findByScene(source);
//		if (ObjectUtils.isNotEmpty(sysDefaultRole)) {
//			SysRole sysRole = sysDefaultRole.getRole();
//			if (ObjectUtils.isNotEmpty(sysRole)) {
//				return this.register(sysUser, sysRole);
//			}
//		}
//		log.error("[Herodotus] |- Default role for [{}] is not set correct, may case register error!", source);
		return null;
	}

//	public SysUser register(SysUser sysUser, SysRole sysRole) {
//		Set<SysRole> sysRoles = new HashSet<>();
//		sysRoles.add(sysRole);
//		return this.register(sysUser, sysRoles);
//	}

//	public SysUser register(SysUser sysUser, Set<SysRole> sysRoles) {
//		if (CollectionUtils.isNotEmpty(sysRoles)) {
//			sysUser.setRoles(sysRoles);
//		}
//		return saveAndFlush(sysUser);
//	}

	private String enhance(String userName) {
		if (StringUtils.isNotBlank(userName)) {
			SysUser checkedSysUser = this.findByUserName(userName);
			if (ObjectUtils.isNotEmpty(checkedSysUser)) {
				return checkedSysUser.getUserName() + IdUtil.nanoId(6);
			} else {
				return userName;
			}
		} else {
			return "Herodotus" + IdUtil.nanoId(6);
		}
	}

	public SysUser register(SocialUserDetails socialUserDetails) {
		SysUser sysUser = new SysUser();

		String userName = enhance(socialUserDetails.getUserName());
		sysUser.setUserName(userName);

		String nickName = socialUserDetails.getNickName();
		if (StringUtils.isNotBlank(nickName)) {
			sysUser.setNickName(nickName);
		}

		String phoneNumber = socialUserDetails.getPhoneNumber();
		if (StringUtils.isNotBlank(phoneNumber)) {
			sysUser.setPhoneNumber(SecurityUtils.encrypt(phoneNumber));
		}

		String avatar = socialUserDetails.getAvatar();
		if (StringUtils.isNotBlank(avatar)) {
			sysUser.setAvatar(avatar);
		}

		sysUser.setPassword(SecurityUtils.encrypt("herodotus-cloud"));

		return register(sysUser, AccountType.getAccountType(socialUserDetails.getSource()));
	}

	public HerodotusUser registerUserDetails(SocialUserDetails socialUserDetails) {
		SysUser newSysUser = register(socialUserDetails);
//		return UpmsHelper.convertSysUserToHerodotusUser(newSysUser);
		return null;
	}

	public void changeStatus(String userId, DataItemStatus status) {
		SysUser sysUser = findByUserId(userId);
		if (ObjectUtils.isNotEmpty(sysUser)) {
			sysUser.setStatus(status);
			log.debug("[Herodotus] |- Change user [{}] status to [{}]", sysUser.getUserName(), status.name());
//			save(sysUser);
		}
	}
}