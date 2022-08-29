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

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.mapper.IDictMapper;
import com.taotao.cloud.sys.biz.repository.cls.DictRepository;
import com.taotao.cloud.sys.biz.repository.inf.IDictRepository;
import com.taotao.cloud.sys.biz.service.business.IDictService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

/**
 * DictServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:26:36
 */
@AllArgsConstructor
@Service
public class DictServiceImpl extends
	BaseSuperServiceImpl<IDictMapper, Dict, DictRepository, IDictRepository, Long>
	implements IDictService {

	private final DictRepository sysDictRepository;

	//private final IDictItemService sysDictItemService;
	//
	//public DictServiceImpl(DictRepository sysDictRepository,
	//	IDictItemService sysDictItemService) {
	//	this.sysDictRepository = sysDictRepository;
	//	this.sysDictItemService = sysDictItemService;
	//}
	//
	//private final QDict SYS_DICT = QDict.sysDict;
	//private final BooleanExpression PREDICATE = SYS_DICT.eq(SYS_DICT);
	//private final OrderSpecifier<Integer> SORT_DESC = SYS_DICT.sortNum.desc();
	//private final OrderSpecifier<LocalDateTime> CREATE_TIME_DESC = SYS_DICT.createTime.desc();
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Dict save(Dict sysDict) {
	//	String dictCode = sysDict.getDictCode();
	//	if (sysDictRepository.existsByDictCode(dictCode)) {
	//		throw new BusinessException(ResultEnum.DICT_CODE_REPEAT_ERROR);
	//	}
	//	return sysDictRepository.saveAndFlush(sysDict);
	//}
	//
	//@Override
	//public List<Dict> getAll() {
	//	return sysDictRepository.findAll();
	//}
	//
	//@Override
	//public Page<Dict> queryPage(Pageable page, DictPageQuery dictPageQuery) {
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
	//	Optional<Dict> optionalDict = sysDictRepository.findById(id);
	//	optionalDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
	//	sysDictRepository.deleteById(id);
	//	sysDictItemService.deleteByDictId(id);
	//	return true;
	//}
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Boolean deleteByCode(String code) {
	//	Dict dict = findByCode(code);
	//	sysDictRepository.delete(dict);
	//
	//	Long dictId = dict.getId();
	//	sysDictItemService.deleteByDictId(dictId);
	//	return true;
	//}
	//
	//@Override
	//public Dict findById(Long id) {
	//	Optional<Dict> optionalDict = sysDictRepository.findById(id);
	//	return optionalDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
	//}
	//

	//@Override
	//public Dict update(Dict dict) {
	//	return sysDictRepository.saveAndFlush(dict);
	//}

	@Override
	public Dict findByCode(String code) {
		//Optional<Dict> optionalDict = sysDictRepository.findByCode(code);
		//return optionalDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
		return Dict.builder().id(2L).createBy(2L).createTime(LocalDateTime.now()).dictCode("123123123").dictName("lsdfjaslf")
			.remark("sdfasfd")
			.description("sdflasjdfl")
			.build();
	}

	@Override
	@Async
	public Future<Dict> findAsyncByCode(String code) {
		LogUtils.info("异步查询字典, 当前线程名称：{}", Thread.currentThread().getName());

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}

		Dict result = Dict.builder().id(2L).createBy(2L).createTime(LocalDateTime.now())
			.dictCode("async123123123").dictName("asynclsdfjaslf")
			.remark("asyncsdfasfd")
			.description("asyncsdflasjdfl")
			.build();

		Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

		LogUtils.info("findAsyncByCode: {}", result);

		return new AsyncResult<>(result);
	}

	@Override
	public String async() {
		return "sdfasf";
	}
}
