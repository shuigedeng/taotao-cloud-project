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

package com.taotao.cloud.sys.application.command.dict.executor;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.application.adapter.DictAdapter;
import com.taotao.cloud.sys.application.command.dict.dto.DictUpdateCmd;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.DictCO;
import com.taotao.cloud.sys.application.converter.DictConvert;
import com.taotao.cloud.sys.domain.dict.service.DictDomainService;
import com.taotao.cloud.sys.infrastructure.persistent.dict.dataobject.DictDO;
import com.taotao.cloud.sys.infrastructure.persistent.dict.mapper.DictMapper;
import com.taotao.cloud.sys.infrastructure.persistent.dict.po.DictPO;
import jakarta.transaction.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 修改字典执行器.
 */
@Component
@RequiredArgsConstructor
public class DictUpdateCmdExe {

	private final DictDomainService dictDomainService;
	private final DictAdapter dictAdapter;
	private final DictConvert dictConvert;
	private final DictMapper dictMapper;

	/**
	 * 执行修改字典.
	 * @param cmd 修改字典参数
	 * @return 执行修改结果
	 */
	@DS(TENANT)
	public Boolean execute(DictUpdateCmd cmd) {
		DictCO co = cmd.getDictCO();
		Long id = co.getId();
		if (ObjectUtil.isNull(id)) {
			throw new BusinessException(ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
		}
		String type = co.getType();
		String value = co.getValue();
		Long count = dictMapper.selectCount(Wrappers.lambdaQuery(DictPO.class)
			.eq(DictPO::getValue, value)
			.eq(DictPO::getType, type)
			.ne(DictPO::getId, co.getId()));
		if (count > 0) {
			throw new BusinessException(String.format("类型为%s，值为%s的字典已存在，请重新填写", type, value));
		}
		return dictDomainService.update(dictConvertor.toEntity(co));
	}

}
