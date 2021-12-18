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
package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.api.dubbo.IDubboDictItemService;
import com.taotao.cloud.sys.biz.entity.SysDictItem;
import com.taotao.cloud.sys.biz.mapper.ISysDictItemMapper;
import com.taotao.cloud.sys.biz.repository.cls.SysDictItemRepository;
import com.taotao.cloud.sys.biz.repository.inf.ISysDictItemRepository;
import com.taotao.cloud.sys.biz.service.ISysDictItemService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * SysDictItemServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:34:52
 */
@Service
@DubboService(interfaceClass = IDubboDictItemService.class)
public class SysDictItemServiceImpl extends
	BaseSuperServiceImpl<ISysDictItemMapper, SysDictItem, SysDictItemRepository, ISysDictItemRepository, Long>
	implements IDubboDictItemService, ISysDictItemService {

	//private final static QSysDictItem SYS_DICT_ITEM = QSysDictItem.sysDictItem;
	//private final static BooleanExpression PREDICATE = SYS_DICT_ITEM.delFlag.eq(false);
	//private final static OrderSpecifier<LocalDateTime> CREATE_TIME_DESC = SYS_DICT_ITEM.createTime.desc();

	//private final SysDictItemRepository dictItemRepository;
	//
	//public SysDictItemServiceImpl(
	//	SysDictItemRepository dictItemRepository) {
	//	this.dictItemRepository = dictItemRepository;
	//}
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Boolean deleteByDictId(Long dictId) {
	//	return dictItemRepository.deleteByDictId(dictId);
	//}
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public SysDictItem save(DictItemDTO dictItemDTO) {
	//	//SysDictItem item = SysDictItem.builder().build();
	//	//BeanUtil.copyIgnoredNull(dictItemDTO, item);
	//	//return dictItemRepository.saveAndFlush(item);
	//	return null;
	//}
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public SysDictItem updateById(Long id, DictItemDTO dictItemDTO) {
	//	Optional<SysDictItem> optionalSysDictItem = dictItemRepository.findById(id);
	//	SysDictItem item = optionalSysDictItem.orElseThrow(() -> new BusinessException("字典项数据不存在"));
	//	BeanUtil.copyIgnoredNull(dictItemDTO, item);
	//	return dictItemRepository.saveAndFlush(item);
	//}
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Boolean deleteById(Long id) {
	//
	//	dictItemRepository.deleteById(id);
	//	return true;
	//}
	//
	//@Override
	//public Page<SysDictItem> getPage(Pageable page, DictItemPageQuery dictItemPageQuery) {
	//	Optional.ofNullable(dictItemPageQuery.getDictId())
	//		.ifPresent(dictId -> PREDICATE.and(SYS_DICT_ITEM.dictId.eq(dictId)));
	//	Optional.ofNullable(dictItemPageQuery.getItemText())
	//		.ifPresent(itemText -> PREDICATE.and(SYS_DICT_ITEM.itemText.like(itemText)));
	//	Optional.ofNullable(dictItemPageQuery.getItemValue())
	//		.ifPresent(itemValue -> PREDICATE.and(SYS_DICT_ITEM.itemValue.like(itemValue)));
	//	Optional.ofNullable(dictItemPageQuery.getDescription())
	//		.ifPresent(description -> PREDICATE.and(SYS_DICT_ITEM.description.like(description)));
	//	Optional.ofNullable(dictItemPageQuery.getStatus())
	//		.ifPresent(status -> PREDICATE.and(SYS_DICT_ITEM.status.eq(status)));
	//	return dictItemRepository.findPageable(PREDICATE, page, CREATE_TIME_DESC);
	//}
	//
	//@Override
	//public List<SysDictItem> getInfo(DictItemQuery dictItemQuery) {
	//	Optional.ofNullable(dictItemQuery.getDictId())
	//		.ifPresent(dictId -> PREDICATE.and(SYS_DICT_ITEM.dictId.eq(dictId)));
	//	Optional.ofNullable(dictItemQuery.getItemText())
	//		.ifPresent(itemText -> PREDICATE.and(SYS_DICT_ITEM.itemText.like(itemText)));
	//	Optional.ofNullable(dictItemQuery.getItemValue())
	//		.ifPresent(itemValue -> PREDICATE.and(SYS_DICT_ITEM.itemValue.like(itemValue)));
	//	Optional.ofNullable(dictItemQuery.getDescription())
	//		.ifPresent(description -> PREDICATE.and(SYS_DICT_ITEM.description.like(description)));
	//	Optional.ofNullable(dictItemQuery.getStatus())
	//		.ifPresent(status -> PREDICATE.and(SYS_DICT_ITEM.status.eq(status)));
	//	return dictItemRepository.getInfo(PREDICATE);
	//}
}
