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

import com.taotao.cloud.sys.biz.entity.QSysDict;
import com.taotao.cloud.sys.biz.entity.Dict;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 * 字典Repository
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:27:37
 */
@Repository
public class SysDictRepository extends BaseSuperRepository<Dict, Long> {

	public SysDictRepository(EntityManager em) {
		super(Dict.class, em);
	}

	private final static QSysDict SYS_DICT = QSysDict.sysDict;

	/**
	 * existsByDictCode
	 *
	 * @param dictCode dictCode
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:27:59
	 */
	public Boolean existsByDictCode(String dictCode) {
		long count = jpaQueryFactory()
			.selectFrom(SYS_DICT)
			.where(SYS_DICT.dictCode.eq(dictCode))
			.fetchCount();
		return count > 0;
	}

	/**
	 * findByCode
	 *
	 * @param code code
	 * @return {@link Optional&lt;com.taotao.cloud.sys.biz.entity.SysDict&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:28:08
	 */
	public Optional<Dict> findByCode(String code) {
		Dict dict = jpaQueryFactory()
			.selectFrom(SYS_DICT)
			.where(SYS_DICT.dictCode.eq(code))
			.fetchOne();
		return Optional.ofNullable(dict);
	}
}
