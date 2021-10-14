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
package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.data.jpa.repository.JpaSuperRepository;
import com.taotao.cloud.uc.biz.entity.QSysUser;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.entity.SysUser;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 * SysUserRepository
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:51:18
 */
@Repository
public class SysUserRepository extends BaseSuperRepository<SysUser, Long> {

	public SysUserRepository(EntityManager em) {
		super(SysUser.class, em);
	}

	private final static QSysUser SYS_USER = QSysUser.sysUser;

	/**
	 * updatePassword
	 *
	 * @param id          id
	 * @param newPassword newPassword
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:51:23
	 */
	public Boolean updatePassword(Long id, String newPassword) {
		//return jpaQueryFactory
		//	.update(SYS_USER)
		//	.set(SYS_USER.password, newPassword)
		//	.where(SYS_USER.id.eq(id))
		//	.execute() > 0;
		return null;
	}
}
