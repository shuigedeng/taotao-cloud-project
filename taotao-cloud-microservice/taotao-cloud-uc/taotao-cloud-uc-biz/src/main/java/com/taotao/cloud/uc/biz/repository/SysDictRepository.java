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

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.QSysDict;
import com.taotao.cloud.uc.biz.entity.SysDict;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * 字典Repository
 *
 * @author dengtao
 * @since 2020/9/29 18:02
 * @version 1.0.0
 */
@Repository
public class SysDictRepository extends BaseJpaRepository<SysDict, Long> {
    public SysDictRepository(EntityManager em) {
        super(SysDict.class, em);
    }

    private final static QSysDict SYS_DICT = QSysDict.sysDict;

    public Boolean existsByDictCode(String dictCode) {
        long count = jpaQueryFactory.selectFrom(SYS_DICT)
                .where(SYS_DICT.dictCode.eq(dictCode))
                .fetchCount();
        return count > 0;
    }

    public Optional<SysDict> findByCode(String code) {
        SysDict dict = jpaQueryFactory.selectFrom(SYS_DICT)
                .where(SYS_DICT.dictCode.eq(code))
                .fetchOne();
        return Optional.ofNullable(dict);
    }
}
