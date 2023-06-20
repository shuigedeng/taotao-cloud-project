/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
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
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.management.compliance;

import com.taotao.cloud.auth.biz.authentication.stamp.LockedUserDetailsStampManager;
import com.taotao.cloud.auth.biz.management.compliance.event.AccountStatusChanger;
import com.taotao.cloud.security.springsecurity.core.definition.domain.HerodotusUser;
import com.taotao.cloud.security.springsecurity.core.definition.service.EnhanceUserDetailsService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>Description: 账户锁定处理服务 </p>
 *
 * 
 * @date : 2022/7/8 19:25
 */
public class OAuth2AccountStatusManager {

	private static final Logger log = LoggerFactory.getLogger(OAuth2AccountStatusManager.class);

	private final UserDetailsService userDetailsService;
	private final AccountStatusChanger accountStatusChanger;
	private final LockedUserDetailsStampManager lockedUserDetailsStampManager;

	public OAuth2AccountStatusManager(UserDetailsService userDetailsService, AccountStatusChanger accountStatusChanger, LockedUserDetailsStampManager lockedUserDetailsStampManager) {
		this.userDetailsService = userDetailsService;
		this.lockedUserDetailsStampManager = lockedUserDetailsStampManager;
		this.accountStatusChanger = accountStatusChanger;
	}

	private EnhanceUserDetailsService getUserDetailsService() {
		return (EnhanceUserDetailsService) userDetailsService;
	}

	private String getUserId(String username) {
		EnhanceUserDetailsService enhanceUserDetailsService = getUserDetailsService();
		HerodotusUser user = enhanceUserDetailsService.loadHerodotusUserByUsername(username);
		if (ObjectUtils.isNotEmpty(user)) {
			return user.getUserId();
		}

		log.info("[Herodotus] |- Can not found the userid for [{}]", username);
		return null;
	}

	public void lock(String username) {
		String userId = getUserId(username);
		if (ObjectUtils.isNotEmpty(userId)) {
//			accountStatusChanger.process(new UserStatus(userId, DataItemStatus.LOCKING.name()));
			lockedUserDetailsStampManager.put(userId, username);
			log.info("[Herodotus] |- User count [{}] has been locked, and record into cache!", username);
		}
	}

	public void enable(String userId) {
		if (ObjectUtils.isNotEmpty(userId)) {
//			accountStatusChanger.process(new UserStatus(userId, DataItemStatus.ENABLE.name()));
		}
	}

	public void releaseFromCache(String username) {
		String userId = getUserId(username);
		if (ObjectUtils.isNotEmpty(userId)) {
			String value = (String)lockedUserDetailsStampManager.get(userId);
			if (StringUtils.isNotEmpty(value)) {
				this.lockedUserDetailsStampManager.delete(userId);
				log.info("[Herodotus] |- User count [{}] locked info has been release!", username);
			}
		}
	}
}
