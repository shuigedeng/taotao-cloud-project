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
package com.taotao.cloud.uc.biz.repository.impl;

import com.taotao.cloud.uc.biz.entity.QSysRole;
import com.taotao.cloud.uc.biz.entity.QSysUserRole;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 * SysRoleRepository
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:47:05
 */
@Repository
public class SysRoleRepository extends BaseSuperRepository<SysRole, Long> {

	public SysRoleRepository(EntityManager em) {
		super(SysRole.class, em);
	}

	private final static QSysRole SYS_ROLE = QSysRole.sysRole;
	private final static QSysUserRole SYS_USER_ROLE = QSysUserRole.sysUserRole;

	/**
	 * findRoleByUserIds
	 *
	 * @param userIds userIds
	 * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysRole&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:47:10
	 */
	public List<SysRole> findRoleByUserIds(Set<Long> userIds) {
		return jpaQueryFactory()
			.select(SYS_ROLE)
			.from(SYS_ROLE)
			.innerJoin(SYS_USER_ROLE)
			.on(SYS_ROLE.id.eq(SYS_USER_ROLE.roleId))
			.where(SYS_USER_ROLE.userId.in(userIds))
			.fetch();
	}

	/**
	 * findRoleByCodes
	 *
	 * @param codes codes
	 * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysRole&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:47:14
	 */
	public List<SysRole> findRoleByCodes(Set<String> codes) {
		return jpaQueryFactory()
			.selectFrom(SYS_ROLE)
			.where(SYS_ROLE.code.in(codes))
			.fetch();
	}
}
