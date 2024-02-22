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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.application.command.dict.dto.DictDeleteCmd;
import com.taotao.cloud.sys.application.command.dict.dto.DictGetQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictInsertCmd;
import com.taotao.cloud.sys.application.command.dict.dto.DictListQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictOptionListQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictUpdateCmd;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.DictCO;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.OptionCO;
import com.taotao.cloud.sys.application.command.dict.executor.DictDeleteCmdExe;
import com.taotao.cloud.sys.application.command.dict.executor.DictInsertCmdExe;
import com.taotao.cloud.sys.application.command.dict.executor.DictUpdateCmdExe;
import com.taotao.cloud.sys.application.command.dict.executor.query.DictGetQryExe;
import com.taotao.cloud.sys.application.command.dict.executor.query.DictListQryExe;
import com.taotao.cloud.sys.application.command.dict.executor.query.DictOptionListQryExe;
import com.taotao.cloud.sys.application.service.DictsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 字典管理.
 */
@Service
@RequiredArgsConstructor
public class DictsServiceImpl implements DictsService {

	private final DictInsertCmdExe dictInsertCmdExe;

	private final DictUpdateCmdExe dictUpdateCmdExe;

	private final DictDeleteCmdExe dictDeleteCmdExe;

	private final DictOptionListQryExe dictOptionListQryExe;

	private final DictGetQryExe dictGetQryExe;

	private final DictListQryExe dictListQryExe;

	/**
	 * 新增字典.
	 *
	 * @param cmd 新增字典参数
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(DictInsertCmd cmd) {
		return dictInsertCmdExe.execute(cmd);
	}

	/**
	 * 修改字典.
	 *
	 * @param cmd 修改字典参数
	 * @return 修改结果
	 */
	@Override
	public Boolean update(DictUpdateCmd cmd) {
		return dictUpdateCmdExe.execute(cmd);
	}

	/**
	 * 根据ID删除字典.
	 *
	 * @param cmd 根据ID删除字典参数
	 * @return 删除字典
	 */
	@Override
	public Boolean deleteById(DictDeleteCmd cmd) {
		return dictDeleteCmdExe.execute(cmd);
	}

	/**
	 * 根据ID查看字典.
	 *
	 * @param qry 根据ID查看字典参数
	 * @return 字典
	 */
	@Override
	public DictCO getById(DictGetQry qry) {
		return dictGetQryExe.execute(qry);
	}

	/**
	 * 查询字典下拉框选择项列表.
	 *
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	@Override
	public List<OptionCO> optionList(DictOptionListQry qry) {
		return dictOptionListQryExe.execute(qry);
	}

	/**
	 * 查询字典列表.
	 *
	 * @param qry 查询字典列表参数
	 * @return 字典列表
	 */
	@Override
	public IPage<DictCO> list(DictListQry qry) {
		return dictListQryExe.execute(qry);
	}


}
