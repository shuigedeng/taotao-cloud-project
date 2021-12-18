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

import com.taotao.cloud.sys.api.dubbo.IDubboDictService;
import com.taotao.cloud.sys.biz.entity.SysDict;
import com.taotao.cloud.sys.biz.mapper.ISysDictMapper;
import com.taotao.cloud.sys.biz.repository.cls.SysDictRepository;
import com.taotao.cloud.sys.biz.repository.inf.ISysDictRepository;
import com.taotao.cloud.sys.biz.service.ISysDictService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * SysDictServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:26:36
 */
@Service
@DubboService(interfaceClass = IDubboDictService.class)
public class SysDictServiceImpl extends
	BaseSuperServiceImpl<ISysDictMapper, SysDict, SysDictRepository, ISysDictRepository, Long>
	implements IDubboDictService, ISysDictService{

	//private final SysDictRepository sysDictRepository;
	//private final ISysDictItemService sysDictItemService;
	//
	//public SysDictServiceImpl(SysDictRepository sysDictRepository,
	//	ISysDictItemService sysDictItemService) {
	//	this.sysDictRepository = sysDictRepository;
	//	this.sysDictItemService = sysDictItemService;
	//}
	//
	//private final QSysDict SYS_DICT = QSysDict.sysDict;
	//private final BooleanExpression PREDICATE = SYS_DICT.eq(SYS_DICT);
	//private final OrderSpecifier<Integer> SORT_DESC = SYS_DICT.sortNum.desc();
	//private final OrderSpecifier<LocalDateTime> CREATE_TIME_DESC = SYS_DICT.createTime.desc();
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public SysDict save(SysDict sysDict) {
	//	String dictCode = sysDict.getDictCode();
	//	if (sysDictRepository.existsByDictCode(dictCode)) {
	//		throw new BusinessException(ResultEnum.DICT_CODE_REPEAT_ERROR);
	//	}
	//	return sysDictRepository.saveAndFlush(sysDict);
	//}
	//
	//@Override
	//public List<SysDict> getAll() {
	//	return sysDictRepository.findAll();
	//}
	//
	//@Override
	//public Page<SysDict> queryPage(Pageable page, DictPageQuery dictPageQuery) {
	//	Optional.ofNullable(dictPageQuery.getDictName())
	//		.ifPresent(dictName -> PREDICATE.and(SYS_DICT.dictName.like(dictName)));
	//	Optional.ofNullable(dictPageQuery.getDictCode())
	//		.ifPresent(dictCode -> PREDICATE.and(SYS_DICT.dictCode.eq(dictCode)));
	//	Optional.ofNullable(dictPageQuery.getDescription())
	//		.ifPresent(description -> PREDICATE.and(SYS_DICT.description.like(description)));
	//	Optional.ofNullable(dictPageQuery.getRemark())
	//		.ifPresent(remark -> PREDICATE.and(SYS_DICT.remark.like(remark)));
	//	return sysDictRepository.findPageable(PREDICATE, page, SORT_DESC, CREATE_TIME_DESC);
	//}
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Boolean removeById(Long id) {
	//	Optional<SysDict> optionalSysDict = sysDictRepository.findById(id);
	//	optionalSysDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
	//	sysDictRepository.deleteById(id);
	//	sysDictItemService.deleteByDictId(id);
	//	return true;
	//}
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Boolean deleteByCode(String code) {
	//	SysDict dict = findByCode(code);
	//	sysDictRepository.delete(dict);
	//
	//	Long dictId = dict.getId();
	//	sysDictItemService.deleteByDictId(dictId);
	//	return true;
	//}
	//
	//@Override
	//public SysDict findById(Long id) {
	//	Optional<SysDict> optionalSysDict = sysDictRepository.findById(id);
	//	return optionalSysDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
	//}
	//
	//@Override
	//public SysDict findByCode(String code) {
	//	Optional<SysDict> optionalSysDict = sysDictRepository.findByCode(code);
	//	return optionalSysDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
	//}
	//
	//@Override
	//public SysDict update(SysDict dict) {
	//	return sysDictRepository.saveAndFlush(dict);
	//}
}
