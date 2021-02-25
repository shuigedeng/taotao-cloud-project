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

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.QSysUser;
import com.taotao.cloud.uc.biz.entity.SysUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 字典Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class SysUserRepository extends BaseJpaRepository<SysUser, Long> {
    public SysUserRepository(EntityManager em) {
        super(SysUser.class, em);
    }

    private final static QSysUser SYS_USER = QSysUser.sysUser;

    public Boolean updatePassword(Long id, String newPassword) {
        
        long number = jpaQueryFactory.update(SYS_USER)
                .set(SYS_USER.password, newPassword)
                .where(SYS_USER.id.eq(id))
                .execute();
        return number > 0;
    }
}
