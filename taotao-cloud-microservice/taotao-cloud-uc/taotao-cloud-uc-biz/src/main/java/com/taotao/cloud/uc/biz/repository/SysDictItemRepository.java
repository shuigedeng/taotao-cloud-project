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

import com.querydsl.core.types.Predicate;
import com.taotao.cloud.common.enums.DelFlagEnum;
import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.QSysDictItem;
import com.taotao.cloud.uc.biz.entity.SysDictItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 字典Repository
 *
 * @author shuigedeng
 * @since 2020/9/29 18:02
 * @version 1.0.0
 */
@Repository
public class SysDictItemRepository extends BaseJpaRepository<SysDictItem, Long> {
    public SysDictItemRepository(EntityManager em) {
        super(SysDictItem.class, em);
    }

    private final static QSysDictItem SYS_DICT_ITEM = QSysDictItem.sysDictItem;

    public void deleteByDictId(Long dictId) {
        jpaQueryFactory.update(SYS_DICT_ITEM)
                .set(SYS_DICT_ITEM.delFlag, true)
                .where(SYS_DICT_ITEM.dictId.eq(dictId))
                .execute();
    }

    public List<SysDictItem> getInfo(Predicate predicate) {
        return jpaQueryFactory.selectFrom(SYS_DICT_ITEM)
                .where(predicate)
                .fetch();
    }
}
