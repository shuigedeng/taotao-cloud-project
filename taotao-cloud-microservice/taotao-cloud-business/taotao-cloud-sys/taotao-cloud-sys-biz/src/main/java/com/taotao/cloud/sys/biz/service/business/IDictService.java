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

package com.taotao.cloud.sys.biz.service.business;

import com.taotao.cloud.sys.api.model.page.DictPageQuery;
import com.taotao.cloud.sys.api.model.query.DictQuery;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.concurrent.Future;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * IDictService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:32:36
 */
public interface IDictService extends BaseSuperService<Dict, Long> {

    /**
     * 添加字典
     *
     * @param Dict Dict
     * @return {@link Dict }
     * @since 2021-10-09 20:24:04
     */
    Dict saveDict(Dict Dict);

    /**
     * 查询所有字典集合
     *
     * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.Dict&gt; }
     * @since 2021-10-09 20:24:23
     */
    List<Dict> getAll();

    /**
     * 分页查询字典集合
     *
     * @param page page
     * @param pageQuery pageQuery
     * @return {@link Page&lt;com.taotao.cloud.sys.biz.entity.Dict&gt; }
     * @since 2021-10-09 20:24:46
     */
    Page<Dict> queryPage(Pageable page, DictPageQuery pageQuery);

    /**
     * 根据主键Id删除字典
     *
     * @param id id
     * @return {@link Boolean }
     * @since 2021-10-09 20:24:59
     */
    Boolean removeById(Long id);

    Dict update(Dict dict);

    /**
     * 根据code删除字典
     *
     * @param code code
     * @return {@link Boolean }
     * @since 2021-10-09 20:25:07
     */
    Boolean deleteByCode(String code);

    /**
     * 根据id查找字典
     *
     * @param id id
     * @return {@link Dict }
     * @since 2021-10-09 20:25:13
     */
    Dict findById(Long id);

    /**
     * 根据code查找字典
     *
     * @param code code
     * @return {@link Dict }
     * @since 2021-10-09 20:25:30
     */
    Dict findByCode(String code);

    Future<Dict> findAsyncByCode(String code);

    String async();

    Boolean add(String type) throws SQLIntegrityConstraintViolationException;

    Boolean add1();

    Dict testMybatisQueryStructure(DictQuery dictQuery);
}
