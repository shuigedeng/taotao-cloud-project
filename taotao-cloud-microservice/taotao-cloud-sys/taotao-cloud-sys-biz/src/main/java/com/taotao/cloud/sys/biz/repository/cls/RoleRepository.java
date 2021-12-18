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
package com.taotao.cloud.sys.biz.repository.cls;

import com.taotao.cloud.sys.biz.entity.Role;
import com.taotao.cloud.sys.biz.entity.UserRole;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import javax.persistence.EntityManager;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * CompanyMapper
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/10/13 22:50
 */
@Repository
public class RoleRepository  extends BaseSuperRepository<Role, Long> {

	public RoleRepository(EntityManager em) {
		super(Role.class, em);
	}

}
