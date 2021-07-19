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
package com.taotao.cloud.security.service;


import com.taotao.cloud.common.model.SecurityMenu;
import java.util.List;

/**
 * 权限服务
 *
 * @author shuigedeng
 * @since 2020/6/2 15:47
 */
public interface PermissionService {

	/**
	 * 查询当前用户拥有的资源权限
	 *
	 * @param roleCodes 角色code列表，多个以','隔开
	 * @return java.util.List<com.taotao.cloud.auth.model.SecurityUserMenu>
	 * @author shuigedeng
	 * @since 2020/5/12 20:38
	 */
	List<SecurityMenu> findMenuByRoleCodes(String roleCodes);
}
