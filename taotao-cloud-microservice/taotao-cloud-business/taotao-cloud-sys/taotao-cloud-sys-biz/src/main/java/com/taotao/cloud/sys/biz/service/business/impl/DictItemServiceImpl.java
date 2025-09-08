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

package com.taotao.cloud.sys.biz.service.business.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.sys.biz.model.dto.dictItem.DictItemDTO;
import com.taotao.cloud.sys.biz.model.page.DictItemPageQuery;
import com.taotao.cloud.sys.biz.model.query.DictItemQuery;
import com.taotao.cloud.sys.biz.mapper.IDictItemMapper;
import com.taotao.cloud.sys.biz.model.entity.dict.DictItem;
import com.taotao.cloud.sys.biz.repository.cls.DictItemRepository;
import com.taotao.cloud.sys.biz.repository.inf.IDictItemRepository;
import com.taotao.cloud.sys.biz.service.business.IDictItemService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DictItemServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:34:52
 */
@Service
public class DictItemServiceImpl
        implements IDictItemService {

    private static final QDictItem DICT_ITEM = QDictItem.dictItem;
    private static final OrderSpecifier<LocalDateTime> CREATE_TIME_DESC = DICT_ITEM.createTime.desc();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByDictId(Long dictId) {
        cr().deleteById(dictId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictItem save(DictItemDTO dictItemDTO) {
        DictItem item = DictItem.builder().build();
        BeanUtils.copyIgnoredNull(dictItemDTO, item);
        return cr().saveAndFlush(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictItem updateById(Long id, DictItemDTO dictItemDTO) {
        Optional<DictItem> optionalDictItem = cr().findById(id);
        DictItem item = optionalDictItem.orElseThrow(() -> new BusinessException("字典项数据不存在"));
        BeanUtils.copyIgnoredNull(dictItemDTO, item);
        return cr().saveAndFlush(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        cr().deleteById(id);
        return true;
    }

    @Override
    public Page<DictItem> getPage(Pageable page, DictItemPageQuery dictItemPageQuery) {
        BooleanExpression predicate = DICT_ITEM.delFlag.eq(false);
        Optional.ofNullable(dictItemPageQuery.getDictId())
                .ifPresent(dictId -> predicate.and(DICT_ITEM.dictId.eq(dictId)));
        Optional.ofNullable(dictItemPageQuery.getItemText())
                .ifPresent(itemText -> predicate.and(DICT_ITEM.itemText.like(itemText)));
        Optional.ofNullable(dictItemPageQuery.getItemValue())
                .ifPresent(itemValue -> predicate.and(DICT_ITEM.itemValue.like(itemValue)));
        Optional.ofNullable(dictItemPageQuery.getDescription())
                .ifPresent(description -> predicate.and(DICT_ITEM.description.like(description)));
        Optional.ofNullable(dictItemPageQuery.getStatus())
                .ifPresent(status -> predicate.and(DICT_ITEM.status.eq(status)));
        return cr().findPageable(predicate, page, CREATE_TIME_DESC);
    }

    @Override
    public List<DictItem> getInfo(DictItemQuery dictItemQuery) {
        BooleanExpression predicate = DICT_ITEM.delFlag.eq(false);
        Optional.ofNullable(dictItemQuery.getDictId()).ifPresent(dictId -> predicate.and(DICT_ITEM.dictId.eq(dictId)));
        Optional.ofNullable(dictItemQuery.getItemText())
                .ifPresent(itemText -> predicate.and(DICT_ITEM.itemText.like(itemText)));
        Optional.ofNullable(dictItemQuery.getItemValue())
                .ifPresent(itemValue -> predicate.and(DICT_ITEM.itemValue.like(itemValue)));
        Optional.ofNullable(dictItemQuery.getDescription())
                .ifPresent(description -> predicate.and(DICT_ITEM.description.like(description)));
        Optional.ofNullable(dictItemQuery.getStatus()).ifPresent(status -> predicate.and(DICT_ITEM.status.eq(status)));
        return cr().find(predicate, DICT_ITEM, CREATE_TIME_DESC);
    }
}
