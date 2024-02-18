/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.taotao.cloud.sys.application.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.application.adapter.DictAdapter;
import com.taotao.cloud.sys.application.command.dict.DictDeleteCmd;
import com.taotao.cloud.sys.application.command.dict.DictGetQry;
import com.taotao.cloud.sys.application.command.dict.DictInsertCmd;
import com.taotao.cloud.sys.application.command.dict.DictListQry;
import com.taotao.cloud.sys.application.command.dict.DictOptionListQry;
import com.taotao.cloud.sys.application.command.dict.DictUpdateCmd;
import com.taotao.cloud.sys.application.command.dict.clientobject.DictCO;
import com.taotao.cloud.sys.application.command.dict.clientobject.OptionCO;
import com.taotao.cloud.sys.application.converter.DictConvert;
import com.taotao.cloud.sys.application.service.DictsCommandService;
import com.taotao.cloud.sys.domain.dict.entity.Dict;
import com.taotao.cloud.sys.domain.dict.service.DictDomainService;
import com.taotao.cloud.sys.infrastructure.persistent.dict.dataobject.DictDO;
import com.taotao.cloud.sys.infrastructure.persistent.dict.mapper.DictMapper;
import com.taotao.cloud.sys.infrastructure.persistent.dict.po.DictPO;
import jakarta.transaction.SystemException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典管理.
 */
@Service
@RequiredArgsConstructor
public class DictsCommandServiceImpl implements DictsCommandService {
	private final DictDomainService dictDomainService;
	private final DictAdapter dictAdapter;
	private final DictConvert dictConvert;
	private final DictMapper dictMapper;

	@DS(TENANT)
	@Override
	public Boolean insert(DictInsertCmd cmd) {
		DictCO co = cmd.getDictCO();
		String type = co.getType();
		String value = co.getValue();
		Long count = dictMapper
			.selectCount(Wrappers.lambdaQuery(DictDO.class).eq(DictDO::getValue, value).eq(DictDO::getType, type));
		if (count > 0) {
			throw new BusinessException(String.format("类型为%s，值为%s的字典已存在，请重新填写", type, value));
		}
		return dictDomainService.insert(dictAdapter.toEntity(co));
	}

	@DS(TENANT)
	@Override
	public Boolean update(DictUpdateCmd cmd) {
		DictCO co = cmd.getDictCO();
		Long id = co.getId();
		if (ObjectUtil.isNull(id)) {
			throw new BusinessException(ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
		}
		String type = co.getType();
		String value = co.getValue();
		Long count = dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getValue, value)
			.eq(DictDO::getType, type)
			.ne(DictDO::getId, co.getId()));
		if (count > 0) {
			throw new SystemException(String.format("类型为%s，值为%s的字典已存在，请重新填写", type, value));
		}
		return dictGateway.update(dictConvertor.toEntity(co));
	}

	@Override
	public Boolean deleteById(DictDeleteCmd cmd) {
		return dictGateway.deleteById(cmd.getId());
	}

	@Override
	public DictCO getById(DictGetQry qry) {
		return Result.of(dictConvertor.convertClientObject(dictGateway.getById(qry.getId())));
	}

	@Override
	public List<OptionCO> optionList(DictOptionListQry qry) {
		List<DictDO> list = dictMapper.selectList(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getType, qry.getType())
			.select(DictDO::getLabel, DictDO::getValue)
			.orderByDesc(DictDO::getId));
		if (CollectionUtil.isEmpty(list)) {
			return Result.of(new ArrayList<>(0));
		}
		return Result.of(ConvertUtil.sourceToTarget(list, OptionCO.class));
	}

	@Override
	public Datas<DictCO> list(DictListQry qry) {
		Dict dict = dictConvert.convert(qry);
		Datas<Dict> datas = dictDomainService.list(dict, qry);
		Datas<DictCO> da = new Datas<>();

		da.setRecords(dictConvertor.convertClientObjectList(datas.getRecords()));
		da.setTotal(datas.getTotal());
		return Result.of(da);
	}

}
