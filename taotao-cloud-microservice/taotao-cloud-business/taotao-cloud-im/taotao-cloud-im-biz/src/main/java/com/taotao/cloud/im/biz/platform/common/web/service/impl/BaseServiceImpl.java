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

package com.taotao.cloud.im.biz.platform.common.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.platform.common.core.EnumUtils;
import com.platform.common.exception.BaseException;
import com.platform.common.web.dao.BaseDao;
import com.platform.common.web.domain.SearchVo;
import com.platform.common.web.enums.SearchTypeEnum;
import com.platform.common.web.service.BaseService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/** 基础service实现层基类 */
public class BaseServiceImpl<T> implements BaseService<T> {

    private BaseDao<T> baseDao;

    public void setBaseDao(BaseDao<T> baseDao) {
        this.baseDao = baseDao;
    }

    private static final Integer batchCount = 1000;

    @Override
    public Integer add(T entity) {
        return baseDao.insert(entity);
    }

    @Override
    public Integer deleteById(Long id) {
        return baseDao.deleteById(id);
    }

    @Override
    public Integer deleteByIds(Long[] ids) {
        return baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public Integer deleteByIds(List<Long> ids) {
        return baseDao.deleteBatchIds(ids);
    }

    @Override
    public Integer updateById(T entity) {
        return baseDao.updateById(entity);
    }

    /**
     * eq: Wrapper wrapper = Wrappers.<ChatFriend>lambdaUpdate().set(ChatFriend::getRemark,
     * null).eq(ChatFriend::getId, friend.getId());
     */
    @Override
    public Integer update(Wrapper wrapper) {
        return baseDao.update(null, wrapper);
    }

    @Override
    public T getById(Long id) {
        return baseDao.selectById(id);
    }

    @Override
    public T findById(Long id) {
        T t = getById(id);
        if (t == null) {
            throw new BaseException("对象不存在");
        }
        return t;
    }

    @Override
    public List<T> getByIds(Collection<? extends Serializable> idList) {
        return baseDao.selectBatchIds(idList);
    }

    @Override
    public Long queryCount(T t) {
        Wrapper<T> wrapper = new QueryWrapper<>(t);
        return baseDao.selectCount(wrapper).longValue();
    }

    @Override
    public List<T> queryList(T t) {
        Wrapper<T> wrapper = new QueryWrapper<>(t);
        return baseDao.selectList(wrapper);
    }

    @Override
    public T queryOne(T t) {
        Wrapper<T> wrapper = new QueryWrapper<>(t);
        return baseDao.selectOne(wrapper);
    }

    @Override
    public Integer batchAdd(List<T> list) {
        return batchAdd(list, batchCount);
    }

    @Transactional
    @Override
    public Integer batchAdd(List<T> list, Integer batchCount) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        List<T> batchList = new ArrayList<>();
        AtomicReference<Integer> result = new AtomicReference<>(0);
        list.forEach((o) -> {
            batchList.add(o);
            if (batchList.size() == batchCount) {
                result.updateAndGet(v -> v + baseDao.insertBatchSomeColumn(batchList));
                batchList.clear();
            }
        });
        if (!CollectionUtils.isEmpty(batchList)) {
            result.updateAndGet(v -> v + baseDao.insertBatchSomeColumn(batchList));
            batchList.clear();
        }
        return result.get();
    }

    @Override
    public List<T> search(List<SearchVo> searchList) {
        QueryWrapper wrappers = new QueryWrapper();
        for (SearchVo searchVo : searchList) {
            SearchTypeEnum searchType =
                    EnumUtils.toEnum(SearchTypeEnum.class, searchVo.getCondition(), SearchTypeEnum.EQ);
            String name = searchVo.getName();
            String value = searchVo.getValue();
            String value2 = searchVo.getValue2();
            switch (searchType) {
                case EQ:
                    wrappers.eq(name, value);
                    break;
                case NE:
                    wrappers.ne(name, value);
                    break;
                case GT:
                    wrappers.gt(name, value);
                    break;
                case GE:
                    wrappers.ge(name, value);
                    break;
                case LT:
                    wrappers.lt(name, value);
                    break;
                case LE:
                    wrappers.le(name, value);
                    break;
                case BETWEEN:
                    wrappers.between(name, value, value2);
                    break;
                case NOT_BETWEEN:
                    wrappers.notBetween(name, value, value2);
                    break;
                case LIKE:
                    wrappers.like(name, value);
                    break;
                case NOT_LIKE:
                    wrappers.notLike(name, value);
                    break;
                case LIKE_LEFT:
                    wrappers.likeLeft(name, value);
                    break;
                case LIKE_RIGHT:
                    wrappers.likeRight(name, value);
                    break;
            }
        }
        return baseDao.search(wrappers);
    }

    /** 响应请求分页数据 */
    protected PageInfo getPageInfo(List<?> list, List<?> oldList) {
        Long total = new PageInfo(oldList).getTotal();
        return getPageInfo(list, total);
    }

    /** 格式化分页 */
    public PageInfo getPageInfo(List<?> list, long total) {
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setTotal(total);
        return pageInfo;
    }
}
