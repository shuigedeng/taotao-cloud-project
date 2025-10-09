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

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.common.RandomUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.MdcUtils;
import com.taotao.boot.data.jpa.model.SelectBooleanBuilder;
import com.taotao.boot.data.jpa.model.SelectBuilder;
import com.taotao.cloud.sys.biz.model.page.DictPageQuery;
import com.taotao.cloud.sys.biz.model.query.DictQuery;
import com.taotao.cloud.sys.biz.mapper.IDictMapper;
import com.taotao.cloud.sys.biz.model.bo.DictDeptBO;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.model.entity.dict.QDict;
import com.taotao.cloud.sys.biz.model.params.DictDeptParams;
import com.taotao.cloud.sys.biz.repository.DictRepository;
import com.taotao.cloud.sys.biz.repository.IDictRepository;
import com.taotao.cloud.sys.biz.service.business.IDictItemService;
import com.taotao.cloud.sys.biz.service.business.IDictService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import org.apache.seata.spring.annotation.GlobalTransactional;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * DictServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:26:36
 */
@AllArgsConstructor
@Service
public class DictServiceImpl extends BaseSuperServiceImpl< Dict, Long,IDictMapper, DictRepository, IDictRepository>
	implements IDictService {

	private final IDictItemService dictItemService;
	private final CriteriaBuilderFactory criteriaBuilderFactory;

	private final QDict DICT = QDict.dict;
	private final OrderSpecifier<Integer> SORT_DESC = DICT.sortNum.desc();
	private final OrderSpecifier<LocalDateTime> CREATE_TIME_DESC = DICT.createTime.desc();

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Dict saveDict(Dict dict) {
		String dictCode = dict.getDictCode();
		if (cr().existsByDictCode(dictCode)) {
			throw new BusinessException(ResultEnum.DICT_CODE_REPEAT_ERROR);
		}
		return cr().saveAndFlush(dict);
	}

	@Override
	public List<Dict> getAll() {
		return cr().findAll();
	}

	@Override
	public Page<Dict> queryPage(Pageable page, DictPageQuery dictPageQuery) {
		BooleanExpression predicate = DICT.delFlag.eq(false);
		Optional.ofNullable(dictPageQuery.getDictName())
			.ifPresent(dictName -> predicate.and(DICT.dictName.like(dictName)));
		Optional.ofNullable(dictPageQuery.getDictCode())
			.ifPresent(dictCode -> predicate.and(DICT.dictCode.eq(dictCode)));
		Optional.ofNullable(dictPageQuery.getDescription())
			.ifPresent(description -> predicate.and(DICT.description.like(description)));
		Optional.ofNullable(dictPageQuery.getRemark()).ifPresent(remark -> predicate.and(DICT.remark.like(remark)));
		return cr().findPageable(predicate, page, SORT_DESC, CREATE_TIME_DESC);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeById(Long id) {
		Optional<Dict> optionalDict = cr().findById(id);
		optionalDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
		cr().deleteById(id);
		dictItemService.deleteByDictId(id);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteByCode(String code) {
		Dict dict = findByCode(code);
		cr().delete(dict);

		Long dictId = dict.getId();
		dictItemService.deleteByDictId(dictId);
		return true;
	}

	@Override
	public Dict findById(Long id) {
		Optional<Dict> optionalDict = cr().findById(id);
		return optionalDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
	}

	@Override
	public Dict update(Dict dict) {
		return cr().saveAndFlush(dict);
	}

	@Override
	public Dict findByCode(String code) {
		// List<Dict> all = ir().findAll();
		// List<Dict> all1 = cr().findAll();

		Optional<Dict> optionalDict = cr().findByCode(code);
		return optionalDict.orElseThrow(() -> new BusinessException(ResultEnum.DICT_NOT_EXIST));
		// return Dict.builder().id(2L).createBy(2L).createTime(LocalDateTime.now())
		//	.dictCode("123123123").dictName("lsdfjaslf")
		//	.remark("sdfasfd")
		//	.description("sdflasjdfl")
		//	.build();
	}

	@Override
	@Async
	public Future<Dict> findAsyncByCode(String code) {
		LogUtils.info("异步查询字典, 当前线程名称：{}", Thread.currentThread().getName());

		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignored) {
		}

		Dict result = Dict.builder()
			.id(2L)
			.createBy(2L)
			.createTime(LocalDateTime.now())
			.dictCode("async123123123")
			.dictName("asynclsdfjaslf")
			.remark("asyncsdfasfd")
			.description("asyncsdflasjdfl")
			.build();

		Map<String, String> copyOfContextMap = MdcUtils.getCopyOfContextMap();

		LogUtils.info("findAsyncByCode: {}", result);

		return CompletableFuture.completedFuture(result);
	}

	@Override
	public String async() {
		return "sdfasf";
	}

	@Override
	@GlobalTransactional(name = "sys-dict-global-transactional-1", rollbackFor = Exception.class)
	public Boolean add(String type) throws SQLIntegrityConstraintViolationException {

		if ("1".equals(type)) {
			throw new BusinessException("xxxxxx");
		}
		if ("2".equals(type)) {
			throw new SQLIntegrityConstraintViolationException("SQLIntegrityConstraintViolationException");
		}

		Dict d1 =
			Dict.builder().dictCode("asdfsadf").dictName("sldf").sortNum(3).build();
		Dict d2 = Dict.builder()
			.dictCode("asdfsadf222")
			.dictName("sldf222")
			.sortNum(5)
			.build();
		int i = im().insertBatchSomeColumn(List.of(d1, d2));

		return true;
	}

	@Override
	// @GlobalTransactional(name = "sys-dict-global-transactional-2", rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	public Boolean add1() {

		String s1 = RandomUtils.randomChar(6);
		String s2 = RandomUtils.randomChar(6);

		Dict d1 = Dict.builder().dictCode(s1).dictName("sldf").sortNum(3).build();
		Dict d2 = Dict.builder().dictCode(s2).dictName("sldf222").sortNum(5).build();
		// int i = im().insertBatchSomeColumn(List.of(d1, d2));

		List<Dict> dicts = cr().saveAll(List.of(d1, d2));

		String s5 = RandomUtils.randomChar(6);
		Dict d5 = Dict.builder().dictCode(s5).dictName("sldf").sortNum(3).build();
		Dict save = cr().save(d5);

		String s3 = RandomUtils.randomChar(6);
		String s4 = RandomUtils.randomChar(6);
		Dict d3 = Dict.builder().dictCode(s3).dictName("sldf").sortNum(3).build();
		Dict d4 = Dict.builder().dictCode(s4).dictName("sldf222").sortNum(5).build();

		String s6 = RandomUtils.randomChar(6);
		Dict d6 = Dict.builder().dictCode(s6).dictName("sldf222").sortNum(5).build();
		Dict save1 = ir().save(d6);

		List<Dict> dicts1 = ir().saveAllAndFlush(List.of(d3, d4));

		return true;
	}

	@Override
	public Dict testMybatisQueryStructure(DictQuery dictQuery) {
		DictDeptParams params = new DictDeptParams();
		params.setIds(List.of(dictQuery.getDictId()));
		List<DictDeptBO> dictDeptBos = this.baseMapper.testMybatisQueryStructure(params);
		LogUtils.info(dictDeptBos.toString());
		return new Dict();
	}

	// ****************************************************************

	public void testDynamicQuery(List<String> names, Long id) {
		BooleanBuilder builder = new BooleanBuilder();
		for (String name : names) {
			builder.or(DICT.dictName.equalsIgnoreCase(name));
		}
		if (id != null) {
			builder.and(DICT.id.eq(id));
		}
		List<Dict> dicts =
			cr().jpaQueryFactory().selectFrom(DICT).where(builder).fetch();
		LogUtils.info("dicts: {}",dicts);
	}

	public PagedList<Dict> testBlazeQuery(@NotNull Long deptId, Dict params, @NotNull Pageable page) {
		// BooleanBuilder builder = SelectBuilder.booleanBuilder(params).getPredicate();
		BooleanBuilder builder = SelectBuilder.booleanBuilder().getPredicate();
		builder.and(DICT.id.eq(deptId));
		return new BlazeJPAQuery<Dict>(cr().entityManager(), criteriaBuilderFactory)
			.select(DICT)
			.from(DICT)
			.leftJoin(DICT)
			.on(DICT.id.eq(DICT.id))
			.where(builder)
			.orderBy(DICT.id.desc())
			.fetchPage((int) page.getOffset(), page.getPageSize());
	}

	public List<Dict> testBlazeQuery(Long projectId, Set<Long> ids, String name, Long limit) {
		if (Objects.isNull(projectId)) {
			return Lists.newArrayList();
		}
		BooleanBuilder where = SelectBooleanBuilder.booleanBuilder()
			.and(DICT.id.eq(projectId))
			.notEmptyIn(ids, DICT.id)
			.notBlankContains(name, DICT.dictName)
			.getPredicate();
		JPAQuery<Dict> query = cr().jpaQueryFactory().selectFrom(DICT).where(where);
		if (limit != null && limit > 0) {
			query.limit(limit);
		}
		return query.fetch();
	}
}
