package com.taotao.cloud.uc.biz.service.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.core.utils.BeanUtil;
import com.taotao.cloud.uc.api.dto.dictItem.DictItemDTO;
import com.taotao.cloud.uc.api.query.dictItem.DictItemPageQuery;
import com.taotao.cloud.uc.api.query.dictItem.DictItemQuery;
import com.taotao.cloud.uc.biz.entity.QSysDictItem;
import com.taotao.cloud.uc.biz.entity.SysDictItem;
import com.taotao.cloud.uc.biz.repository.SysDictItemRepository;
import com.taotao.cloud.uc.biz.service.ISysDictItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SysDictItemServiceImpl
 *
 * @author dengtao
 * @date 2020/4/30 11:24
 */
@Service
@AllArgsConstructor
public class SysDictItemServiceImpl implements ISysDictItemService {
    private final static QSysDictItem SYS_DICT_ITEM = QSysDictItem.sysDictItem;
    private final static BooleanExpression PREDICATE = SYS_DICT_ITEM.delFlag.eq(false);
    private final static OrderSpecifier<LocalDateTime> CREATE_TIME_DESC = SYS_DICT_ITEM.createTime.desc();

    private final SysDictItemRepository dictItemRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDictId(Long dictId) {
        dictItemRepository.deleteByDictId(dictId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysDictItem save(DictItemDTO dictItemDTO) {
        SysDictItem item = SysDictItem.builder().build();
        BeanUtil.copyIgnoredNull(dictItemDTO, item);
        return dictItemRepository.saveAndFlush(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysDictItem updateById(Long id, DictItemDTO dictItemDTO) {
        Optional<SysDictItem> optionalSysDictItem = dictItemRepository.findById(id);
        SysDictItem item = optionalSysDictItem.orElseThrow(() -> new BusinessException("字典项数据不存在"));
        BeanUtil.copyIgnoredNull(dictItemDTO, item);
        return dictItemRepository.saveAndFlush(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        dictItemRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<SysDictItem> getPage(Pageable page, DictItemPageQuery dictItemPageQuery) {
        Optional.ofNullable(dictItemPageQuery.getDictId())
                .ifPresent(dictId -> PREDICATE.and(SYS_DICT_ITEM.dictId.eq(dictId)));
        Optional.ofNullable(dictItemPageQuery.getItemText())
                .ifPresent(itemText -> PREDICATE.and(SYS_DICT_ITEM.itemText.like(itemText)));
        Optional.ofNullable(dictItemPageQuery.getItemValue())
                .ifPresent(itemValue -> PREDICATE.and(SYS_DICT_ITEM.itemValue.like(itemValue)));
        Optional.ofNullable(dictItemPageQuery.getDescription())
                .ifPresent(description -> PREDICATE.and(SYS_DICT_ITEM.description.like(description)));
        Optional.ofNullable(dictItemPageQuery.getStatus())
                .ifPresent(status -> PREDICATE.and(SYS_DICT_ITEM.status.eq(status)));
        return dictItemRepository.findAll(PREDICATE, page, CREATE_TIME_DESC);
    }

    @Override
    public List<SysDictItem> getInfo(DictItemQuery dictItemQuery) {
        Optional.ofNullable(dictItemQuery.getDictId())
                .ifPresent(dictId -> PREDICATE.and(SYS_DICT_ITEM.dictId.eq(dictId)));
        Optional.ofNullable(dictItemQuery.getItemText())
                .ifPresent(itemText -> PREDICATE.and(SYS_DICT_ITEM.itemText.like(itemText)));
        Optional.ofNullable(dictItemQuery.getItemValue())
                .ifPresent(itemValue -> PREDICATE.and(SYS_DICT_ITEM.itemValue.like(itemValue)));
        Optional.ofNullable(dictItemQuery.getDescription())
                .ifPresent(description -> PREDICATE.and(SYS_DICT_ITEM.description.like(description)));
        Optional.ofNullable(dictItemQuery.getStatus())
                .ifPresent(status -> PREDICATE.and(SYS_DICT_ITEM.status.eq(status)));
        return dictItemRepository.getInfo(PREDICATE);
    }
}
