/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.sys.infrastructure.persistent.dict.repository.cls;

import com.taotao.cloud.sys.infrastructure.persistent.dict.po.DictPO;
import com.taotao.boot.web.base.repository.BaseClassSuperRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

/**
 * CompanyMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/13 22:50
 */
@Repository
public class DictRepository extends BaseClassSuperRepository<DictPO, Long> {

    public DictRepository(EntityManager em) {
        super(DictPO.class, em);
    }

    public Optional<DictPO> findByCode(String code) {
        // ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        //	.withMatcher("dictCode", GenericPropertyMatcher::exact);
        // Optional<Dict> one = findOne(
        //	Example.of(Dict.builder().dictCode(code).build(), exampleMatcher));

        return findOne((Specification<DictPO>) (root, query, builder) ->
                query.where(builder.equal(root.get("dictCode"), code)).getRestriction());
    }

    public boolean existsByDictCode(String dictCode) {
        return false;
    }
}
