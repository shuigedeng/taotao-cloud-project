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

package com.taotao.cloud.auth.biz.management.service;

import com.taotao.cloud.auth.biz.management.entity.OAuth2Permission;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.management.repository.OAuth2ScopeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p> Description : OauthScopeService </p>
 *
 * 
 * @date : 2020/3/19 17:00
 */
@Service
public class OAuth2ScopeService {

	private final OAuth2ScopeRepository oauthScopesRepository;

	public OAuth2ScopeService(OAuth2ScopeRepository oauthScopesRepository) {
		this.oauthScopesRepository = oauthScopesRepository;
	}


	public OAuth2Scope assigned(String scopeId, Set<OAuth2Permission> permissions) {

		OAuth2Scope oldScope = oauthScopesRepository.findById(scopeId).get();
		oldScope.setPermissions(permissions);

		return oauthScopesRepository.saveAndFlush(oldScope);
	}

	public OAuth2Scope findByScopeCode(String scopeCode) {
		return oauthScopesRepository.findByScopeCode(scopeCode);
	}

	public List<OAuth2Scope> findByScopeCodeIn(List<String> scopeCodes) {
		return oauthScopesRepository.findByScopeCodeIn(scopeCodes);
	}

	public List<OAuth2Scope> findAll() {

		return oauthScopesRepository.findAll();

	}
}
