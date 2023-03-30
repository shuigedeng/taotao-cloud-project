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

package com.taotao.cloud.im.biz.platform.common.web.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.platform.common.web.domain.SearchVo;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/** 基础service接口层基类 */
public interface BaseService<T> {

    /**
     * 新增（并返回id）
     *
     * @param entity
     * @return
     */
    Integer add(T entity);

    /**
     * 根据 id 删除
     *
     * @param id
     * @return
     */
    Integer deleteById(Long id);

    /**
     * 根据ids 批量删除
     *
     * @param ids
     * @return
     */
    Integer deleteByIds(Long[] ids);

    /**
     * 根据ids 批量删除
     *
     * @param ids
     * @return
     */
    Integer deleteByIds(List<Long> ids);

    /**
     * 根据 id 修改
     *
     * @param entity
     * @return
     */
    Integer updateById(T entity);

    /**
     * 根据条件更新
     *
     * @return
     */
    Integer update(Wrapper wrapper);

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * 根据 ids 查询
     *
     * @param idList
     * @return
     */
    List<T> getByIds(Collection<? extends Serializable> idList);

    /**
     * 查询总记录数
     *
     * @param t
     * @return
     */
    Long queryCount(T t);

    /**
     * 查询全部记录
     *
     * @param t
     * @return
     */
    List<T> queryList(T t);

    /**
     * 查询一个
     *
     * @param t
     * @return
     */
    T queryOne(T t);

    /**
     * 批量新增
     *
     * @param list
     * @return
     */
    Integer batchAdd(List<T> list);

    /**
     * 批量新增（仅mysql可以使用）
     *
     * @param list
     * @return
     */
    Integer batchAdd(List<T> list, Integer batchCount);

    /**
     * 动态搜索
     *
     * @param searchList
     * @return
     */
    List<T> search(List<SearchVo> searchList);
}
