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

import com.querydsl.core.types.Predicate;
import com.taotao.cloud.sys.biz.entity.QSysDictItem;
import com.taotao.cloud.sys.biz.entity.DictItem;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 * SysDictItemRepository
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:35:25
 */
@Repository
public class SysDictItemRepository extends BaseSuperRepository<DictItem, Long> {

	public SysDictItemRepository(EntityManager em) {
		super(DictItem.class, em);
	}

	private final static QSysDictItem SYS_DICT_ITEM = QSysDictItem.sysDictItem;

	/**
	 * deleteByDictId
	 *
	 * @param dictId dictId
	 * @author shuigedeng
	 * @since 2021-10-09 20:35:28
	 */
	public Boolean deleteByDictId(Long dictId) {
		return jpaQueryFactory()
			.update(SYS_DICT_ITEM)
			.set(SYS_DICT_ITEM.delFlag, true)
			.where(SYS_DICT_ITEM.dictId.eq(dictId))
			.execute() > 0;
	}

	/**
	 * getInfo
	 *
	 * @param predicate predicate
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysDictItem&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:35:31
	 */
	public List<DictItem> getInfo(Predicate predicate) {
		return jpaQueryFactory()
			.selectFrom(SYS_DICT_ITEM)
			.where(predicate)
			.fetch();
	}
}
